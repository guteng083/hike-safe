package com.haven.app.haven.controller;

import com.haven.app.haven.constant.Gender;
import com.haven.app.haven.dto.request.ChangePasswordRequest;
import com.haven.app.haven.dto.request.LoginRequest;
import com.haven.app.haven.dto.request.RegisterRequest;
import com.haven.app.haven.dto.response.CommonResponse;
import com.haven.app.haven.dto.response.CommonResponseWithData;
import com.haven.app.haven.dto.response.LoginResponse;
import com.haven.app.haven.exception.AuthenticationException;
import com.haven.app.haven.exception.ValidationException;
import com.haven.app.haven.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private ChangePasswordRequest changePasswordRequest;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequest.builder()
                .email("akbar@gmail.com")
                .password("Password")
                .fullName("akbar")
                .birthDate(LocalDate.parse("2000-10-01"))
                .nik("928102819281")
                .gender(Gender.MALE)
                .phone("081388271928")
                .address("Malang")
                .build();

        loginRequest = LoginRequest.builder()
                .email("akbar@gmail.com")
                .password("Password")
                .build();

        changePasswordRequest = ChangePasswordRequest.builder()
                .password("Password")
                .newPassword("NewPassword")
                .confirmPassword("NewPassword")
                .build();
    }

    @Test
    void registerCustomer() throws Exception {
        CommonResponse response = authenticationController.RegisterCustomer(registerRequest);

        verify(authService).registerCustomer(registerRequest);
        assertEquals("Register Success", response.getMessage());
    }

    @Test
    void registerAdmin() throws Exception {
        String secretKey = "1234567890";

        CommonResponse response = authenticationController.RegisterAdmin(registerRequest, secretKey);

        verify(authService).registerAdmin(registerRequest, secretKey);
        assertEquals("Register Success", response.getMessage());
    }

    @Test
    void registerStaff() {
        CommonResponse response = authenticationController.RegisterStaff(registerRequest);

        verify(authService).registerStaff(registerRequest);
        assertEquals("Register Success", response.getMessage());
    }

    @Test
    void login() {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken("testToken");
        when(authService.login(loginRequest)).thenReturn(loginResponse);

        CommonResponseWithData<LoginResponse> response = authenticationController.Login(loginRequest);

        verify(authService).login(loginRequest);
        assertEquals("Success", response.getMessage());
        assertEquals(loginResponse, response.getData());
    }

    @Test
    void changePassword() {
        CommonResponse response = authenticationController.changePassword(changePasswordRequest);

        verify(authService).changePassword(changePasswordRequest);
        assertEquals("Change Password Success", response.getMessage());
    }

    @Test
    void me() {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken("testToken");
        when(authService.getMe()).thenReturn(loginResponse);

        CommonResponseWithData<LoginResponse> response = authenticationController.me();

        verify(authService).getMe();
        assertEquals("Success", response.getMessage());
        assertEquals(loginResponse, response.getData());
    }

    @Test
    void login_AuthenticationFailure() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("wrong@gmail.com")
                .password("Password")
                .build();

        when(authService.login(loginRequest)).thenThrow(new AuthenticationException("Login Failed"));

        assertThrows(AuthenticationException.class, () -> authenticationController.Login(loginRequest));
    }
}