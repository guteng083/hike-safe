package com.haven.app.haven.service.impl;

import com.haven.app.haven.constant.Role;
import com.haven.app.haven.dto.request.ChangePasswordRequest;
import com.haven.app.haven.dto.request.LoginRequest;
import com.haven.app.haven.dto.request.RegisterRequest;
import com.haven.app.haven.dto.response.LoginResponse;
import com.haven.app.haven.entity.Users;
import com.haven.app.haven.entity.UsersDetail;
import com.haven.app.haven.exception.AuthenticationException;
import com.haven.app.haven.exception.ValidationException;
import com.haven.app.haven.service.AuthService;
import com.haven.app.haven.service.JwtService;
import com.haven.app.haven.service.UsersService;
import com.haven.app.haven.utils.LogUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UsersService usersService;
    private final JwtService jwtService;

    @Override
    public void registerAdmin(RegisterRequest request, String secretKey) {
        try {
            String secretAdminKey = "1234567890";
            if (secretKey.equals(secretAdminKey)) {
                usersService.createUser(createUser(request, Role.ROLE_ADMIN));
            } else {
                Map<String, List<String>> errors = new HashMap<>();
                errors.put("SecretKey", Collections.singletonList("SecretKey not match"));
                throw new ValidationException("Register failed", errors);
            }

            LogUtils.logSuccess("AuthService", "registerAdmin");
        } catch (Exception e) {
            LogUtils.getError("AuthService.registerAdmin", e);
        }
    }

    @Override
    public void registerStaff(RegisterRequest request) {
        try {
            usersService.createUser(createUser(request, Role.ROLE_STAFF));

            LogUtils.logSuccess("AuthService", "registerStaff");
        } catch (Exception e) {
            LogUtils.getError("AuthService.registerStaff", e);
        }
    }

    @Override
    public void registerCustomer(RegisterRequest request) {
        try {
            usersService.createUser(createUser(request, Role.ROLE_CUSTOMER));

            LogUtils.logSuccess("AuthService", "registerCustomer");
        } catch (Exception e) {
            LogUtils.getError("AuthService.registerCustomer", e);
        }
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            Users user = (Users) authentication.getPrincipal();
            user.setAccessToken(jwtService.generateToken(user));
            usersService.updateUser(user);

            LogUtils.logSuccess("AuthService", "login");

            return createLoginResponse(user);
        } catch (org.springframework.security.core.AuthenticationException e) {
            LogUtils.getError("AuthService.login", e);
            throw new AuthenticationException("Login Failed");
        }

    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        try {
            Users user = usersService.getMe();
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                Map<String, List<String>> errors = new HashMap<>();
                errors.put("Password", Collections.singletonList("password incorrect"));
                throw new ValidationException("Register failed", errors);
            }

            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                Map<String, List<String>> errors = new HashMap<>();
                errors.put("Password", Collections.singletonList("New Password and Confirm Password do not match"));
                throw new ValidationException("Register failed", errors);
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            usersService.updateUser(user);

            LogUtils.logSuccess("AuthService", "changePassword");
        } catch (Exception e) {
            LogUtils.getError("AuthService.changePassword", e);
        }
    }

    @Override
    public LoginResponse getMe() {
        try {
            return createLoginResponse(usersService.getMe());
        } catch (Exception e) {
            LogUtils.getError("AuthService.getMe", e);
            throw e;
        }
    }

    public static LoginResponse createLoginResponse(Users user) {
        return LoginResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .fullName(user.getUsersDetail().getFullName())
                .birthDate(user.getUsersDetail().getBirthDate())
                .gender(user.getUsersDetail().getGender())
                .address(user.getUsersDetail().getAddress())
                .phone(user.getUsersDetail().getPhone())
                .accessToken(user.getAccessToken())
                .nik(user.getUsersDetail().getNik())
                .imageUrl(user.getUsersDetail().getImageUrl())
                .build();
    }

    private Users createUser(RegisterRequest request, Role role) {
        Users newUser = Users.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        UsersDetail usersDetail = UsersDetail.builder()
                .users(newUser)
                .fullName(request.getFullName())
                .nik(request.getNik())
                .address(request.getAddress())
                .gender(request.getGender())
                .birthDate(request.getBirthDate())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();

        newUser.setUsersDetail(usersDetail);
        return newUser;
    }

}
