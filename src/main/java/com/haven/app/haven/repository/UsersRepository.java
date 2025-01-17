package com.haven.app.haven.repository;

import com.haven.app.haven.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsersRepository extends JpaRepository<Users, String> {
    UserDetails findByEmail(String email);

    boolean existsByEmail(String email);
}
