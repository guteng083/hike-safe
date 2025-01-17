package com.haven.app.haven.entity;

import com.haven.app.haven.constant.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users_detail")
public class UsersDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "users_id")
    private Users users;

    @Column(nullable = false, name = "full_name")
    private String fullName;

    @Column(nullable = false, name = "birth_date")
    private LocalDate birthDate;

    @Column(nullable = false, name = "nik")
    private String nik;

    @Column(nullable = false)
    private String phone;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String address;
}
