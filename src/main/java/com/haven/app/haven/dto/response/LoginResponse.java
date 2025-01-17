package com.haven.app.haven.dto.response;

import com.haven.app.haven.constant.Gender;
import com.haven.app.haven.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String email;
    private Role role;
    private String fullName;
    private LocalDate birthDate;
    private String nik;
    private Gender gender;
    private String address;
    private String phone;
    private String accessToken;
}
