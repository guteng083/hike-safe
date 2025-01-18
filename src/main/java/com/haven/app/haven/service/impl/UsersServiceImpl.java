package com.haven.app.haven.service.impl;

import com.haven.app.haven.entity.Users;
import com.haven.app.haven.exception.ValidationException;
import com.haven.app.haven.repository.UsersRepository;
import com.haven.app.haven.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;

    @Override
    public Users createUser(Users users) {
        if (emailExists(users.getEmail())) {
            Map<String, List<String>> errors = new HashMap<>();
            errors.put("email", Collections.singletonList("email already exist"));
            throw new ValidationException("Register failed", errors);
        }
        return usersRepository.saveAndFlush(users);
    }

    @Override
    public Users getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Users) authentication.getPrincipal();
    }

    @Override
    public void updateUser(Users users) {
        usersRepository.saveAndFlush(users);
    }

    public boolean emailExists(String email) {
        return usersRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByEmail(username);
    }
}
