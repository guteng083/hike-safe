package com.haven.app.haven.service;

import com.haven.app.haven.entity.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService  extends UserDetailsService {
    Users createUser(Users users);
}
