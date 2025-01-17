package com.haven.app.haven.service.impl;

import com.haven.app.haven.constant.Role;
import com.haven.app.haven.dto.request.LoginRequest;
import com.haven.app.haven.dto.request.RegisterRequest;
import com.haven.app.haven.dto.response.LoginResponse;
import com.haven.app.haven.entity.Users;
import com.haven.app.haven.entity.UsersDetail;
import com.haven.app.haven.service.AuthService;
import com.haven.app.haven.service.JwtService;
import com.haven.app.haven.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UsersService usersService;
    private final JwtService jwtService;

    @Override
    public void registerAdmin(RegisterRequest request, String secretKey) {
        String secretAdminKey = "1234567890";
        if (secretKey.equals(secretAdminKey)) {
            usersService.createUser(createUser(request, Role.ROLE_ADMIN));
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");
        }
    }

    @Override
    public void registerStaff(RegisterRequest request) {
        usersService.createUser(createUser(request, Role.ROLE_STAFF));
    }

    @Override
    public void registerCustomer(RegisterRequest request) {
        usersService.createUser(createUser(request, Role.ROLE_CUSTOMER));
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Users user = (Users) authentication.getPrincipal();

        if (authentication.isAuthenticated()) {
            return createLoginResponse(user);
        }
        throw new UsernameNotFoundException("Invalid email or password");
    }

    private LoginResponse createLoginResponse(Users user) {
        return LoginResponse.builder()
                .email(user.getEmail())
                .role(user.getRole())
                .fullName(user.getUsersDetail().getFullName())
                .birthDate(user.getUsersDetail().getBirthDate())
                .gender(user.getUsersDetail().getGender())
                .address(user.getUsersDetail().getAddress())
                .phone(user.getUsersDetail().getPhone())
                .accessToken(jwtService.generateToken(user))
                .nik(user.getUsersDetail().getNik())
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
