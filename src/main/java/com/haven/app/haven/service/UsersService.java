package com.haven.app.haven.service;

import com.haven.app.haven.dto.request.SearchRequest;
import com.haven.app.haven.dto.request.UpdateUserRequest;
import com.haven.app.haven.dto.response.LoginResponse;
import com.haven.app.haven.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UsersService  extends UserDetailsService {
    Users createUser(Users users);
    Users getMe();
    void updateUser(Users users);
    void updateUserDetails(UpdateUserRequest request);
    LoginResponse getStaffById(String staffId);
    Page<LoginResponse> getAllStaff(SearchRequest searchRequest);
    Page<LoginResponse> getAllCustomer(SearchRequest searchRequest);
    LoginResponse getCustomerById(String customerId);
    void updateUserImage(MultipartFile image) throws IOException;
    void deleteStaff(String staffId);
    void resetPassword(String staffId);
}
