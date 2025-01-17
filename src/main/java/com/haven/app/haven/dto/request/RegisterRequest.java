package com.haven.app.haven.dto.request;

import com.haven.app.haven.constant.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private String fullName;
    private LocalDate birthDate;
    private String nik;
    private Gender gender;
    private String phone;
    private String address;
}
