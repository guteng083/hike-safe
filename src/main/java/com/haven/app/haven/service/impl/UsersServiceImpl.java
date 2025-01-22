package com.haven.app.haven.service.impl;

import com.haven.app.haven.constant.Role;
import com.haven.app.haven.dto.request.SearchRequest;
import com.haven.app.haven.dto.request.UpdateUserRequest;
import com.haven.app.haven.dto.response.LoginResponse;
import com.haven.app.haven.dto.response.RegisterResponse;
import com.haven.app.haven.entity.Users;
import com.haven.app.haven.entity.UsersDetail;
import com.haven.app.haven.exception.NotFoundException;
import com.haven.app.haven.exception.ValidationException;
import com.haven.app.haven.repository.UsersRepository;
import com.haven.app.haven.service.CloudinaryService;
import com.haven.app.haven.service.UsersService;
import com.haven.app.haven.specification.UsersSpecification;
import com.haven.app.haven.utils.LogUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Users createUser(Users users) {
        try {
            if (emailExists(users.getEmail())) {
                Map<String, List<String>> errors = new HashMap<>();
                errors.put("email", Collections.singletonList("email already exist"));
                throw new ValidationException("Register failed", errors);
            }

            LogUtils.logSuccess("UsersService", "createUser");

            return usersRepository.saveAndFlush(users);
        } catch (Exception e) {
            LogUtils.getError("UsersService.createUser", e);
            throw e;
        }
    }

    @Override
    public Users getMe() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            LogUtils.logSuccess("UsersService", "getMe");
            return (Users) authentication.getPrincipal();
        } catch (Exception e) {
            LogUtils.getError("UsersService.getMe", e);
            throw e;
        }
    }

    @Override
    public void updateUser(Users users) {
        usersRepository.saveAndFlush(users);
    }

    @Override
    public void updateUserDetails(UpdateUserRequest request) {
        try {
            Users users = getMe();
            UsersDetail userDetail = users.getUsersDetail();
            if (userDetail == null) {
                userDetail = new UsersDetail();
                userDetail.setUsers(users);
            }
            if (request.getFullName() != null) {
                userDetail.setFullName(request.getFullName());
            }
            if (request.getBirthDate() != null) {
                userDetail.setBirthDate(request.getBirthDate());
            }
            if (request.getNik() != null) {
                userDetail.setNik(request.getNik());
            }
            if (request.getGender() != null) {
                userDetail.setGender(request.getGender());
            }
            if (request.getPhone() != null) {
                userDetail.setPhone(request.getPhone());
            }
            if (request.getAddress() != null) {
                userDetail.setAddress(request.getAddress());
            }

            users.setUsersDetail(userDetail);
            updateUser(users);
            LogUtils.logSuccess("UsersService", "updateUserDetails");
        } catch (Exception e) {
            LogUtils.getError("UsersService.updateUser", e);
            if (e instanceof ValidationException) {
                throw new ValidationException("Update user failed", Collections.singletonMap("error", Collections.singletonList(e.getMessage())));
            }

            throw new RuntimeException("Update user failed");

        }
    }

    @Override
    public LoginResponse getStaffById(String staffId) {
        try {
            Users staff = usersRepository.findByIdAndRole(staffId, Role.ROLE_STAFF);
            if (staff == null) {
                throw new NotFoundException("Staff not found");
            }

            LogUtils.logSuccess("UsersService", "getStaffById");

            return AuthServiceImpl.createLoginResponse(staff);
        } catch (Exception e) {
            LogUtils.getError("UsersService.getStaffById", e);
            throw e;
        }
    }

    @Override
    public Page<LoginResponse> getAllStaff(SearchRequest searchRequest) {
        try {
            Pageable pageable = PageRequest.of(searchRequest.getPage() - 1, searchRequest.getSize());

            Specification<Users> usersSpecification = UsersSpecification.getSpecification(searchRequest, Role.ROLE_STAFF);

            Page<Users> users = usersRepository.findAll(usersSpecification, pageable);

            LogUtils.logSuccess("UsersService", "getAllStaff");

            return users.map(AuthServiceImpl::createLoginResponse);
        } catch (Exception e) {
            LogUtils.getError("UsersService.getAllStaff", e);
            throw e;
        }
    }

    @Override
    public Page<LoginResponse> getAllCustomer(SearchRequest searchRequest) {
        try{
            Pageable pageable = PageRequest.of(searchRequest.getPage() - 1, searchRequest.getSize());

            Specification<Users> usersSpecification = UsersSpecification.getSpecification(searchRequest, Role.ROLE_CUSTOMER);

            Page<Users> users = usersRepository.findAll(usersSpecification, pageable);

            LogUtils.logSuccess("UsersService", "getAllCustomer");

            return users.map(AuthServiceImpl::createLoginResponse);
        } catch (Exception e) {
            LogUtils.getError("UsersService.getAllCustomer", e);
            throw e;
        }
    }

    @Override
    public LoginResponse getCustomerById(String customerId) {
        try {
            Users customer = usersRepository.findByIdAndRole(customerId, Role.ROLE_CUSTOMER);

            if(customer == null) {
                throw new NotFoundException("Customer not found");
            }

            LogUtils.logSuccess("UsersService", "getCustomerById");

            return AuthServiceImpl.createLoginResponse(customer);
        } catch (Exception e) {
            LogUtils.getError("UsersService.getCustomerById", e);
            throw new NotFoundException("Customer not found");
        }
    }

    @Override
    public void updateUserImage(MultipartFile image) throws IOException {
        try {
            Users users = getMe();
            String imageUrl = cloudinaryService.uploadImage(image, users.getId());
            users.getUsersDetail().setImageUrl(imageUrl);
            updateUser(users);

            LogUtils.logSuccess("UsersService", "updateUserImage");
        } catch (Exception e) {
            LogUtils.getError("UsersService.updateUserImage", e);
            throw e;
        }
    }

    @Override
    public void deleteStaff(String staffId) {
        try {
            Users staff = usersRepository.findById(staffId)
                    .orElseThrow(() -> new NotFoundException("Staff not found"));

            usersRepository.delete(staff);

            LogUtils.logSuccess("UsersService", "deleteStaff");
        } catch (Exception e) {
            LogUtils.getError("UsersService.deleteStaff", e);
            throw new NotFoundException("Delete staff failed");
        }
    }

    @Override
    public void resetPassword(String staffId) {
        try {
            Users staff = usersRepository.findById(staffId)
                    .orElseThrow(() -> new NotFoundException("Staff not found"));

            String defaultPassword = "password";

            staff.setPassword(passwordEncoder.encode(defaultPassword));
            updateUser(staff);

            LogUtils.logSuccess("UsersService", "resetPassword");
        } catch (Exception e) {
            LogUtils.getError("UsersService.resetPassword", e);
            throw new NotFoundException("Reset password failed");
        }
    }

    public boolean emailExists(String email) {
        return usersRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByEmail(username);
    }
}
