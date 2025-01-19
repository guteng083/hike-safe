package com.haven.app.haven.service.impl;

import com.haven.app.haven.constant.Role;
import com.haven.app.haven.dto.request.UpdateUserRequest;
import com.haven.app.haven.dto.response.LoginResponse;
import com.haven.app.haven.dto.response.RegisterResponse;
import com.haven.app.haven.entity.Users;
import com.haven.app.haven.entity.UsersDetail;
import com.haven.app.haven.exception.NotFoundException;
import com.haven.app.haven.exception.ValidationException;
import com.haven.app.haven.repository.UsersRepository;
import com.haven.app.haven.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;

    @Override
    public Users createUser(Users users) {
        if (emailExists(users.getEmail())) {
            Map<String, List<String>> errors = new HashMap<>();
            errors.put("email", Collections.singletonList("email already exist"));
            throw new ValidationException("Register failed", errors);
        }
        return usersRepository.saveAndFlush(users);
    }

    @Override
    public Users getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Users) authentication.getPrincipal();
    }

    @Override
    public void updateUser(Users users) {
        usersRepository.saveAndFlush(users);
    }

    @Override
    public void updateUserDetails(UpdateUserRequest request) {
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
    }

    @Override
    public LoginResponse getStaffById(String staffId) {
        Users staff = usersRepository.findByIdAndRole(staffId, Role.ROLE_STAFF);
        if (staff == null) {
            throw new NotFoundException("Staff not found");
        }
        return AuthServiceImpl.createLoginResponse(staff);
    }

    @Override
    public Page<LoginResponse> getAllStaff(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Users> users = usersRepository.findAllByRole(Role.ROLE_STAFF,pageable);
        return users.map(AuthServiceImpl::createLoginResponse);
    }

    public boolean emailExists(String email) {
        return usersRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByEmail(username);
    }
}
