package com.haven.app.haven.dto.request;

import com.haven.app.haven.constant.Gender;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String fullName;

    private LocalDate birthDate;

    @Size(min = 16, max = 16, message = "NIK must be 16 characters")
    private String nik;

    private Gender gender;

    @Pattern(regexp = "^\\+?[0-9]{10,13}$", message = "Invalid phone number format")
    private String phone;

    private String address;
}
