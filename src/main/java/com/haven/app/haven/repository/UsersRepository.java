package com.haven.app.haven.repository;

import com.haven.app.haven.constant.Role;
import com.haven.app.haven.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsersRepository extends JpaRepository<Users, String>, JpaSpecificationExecutor<Users> {
    UserDetails findByEmail(String email);

    boolean existsByEmail(String email);

    Users findByIdAndRole(String id, Role role);
}
