package com.haven.app.haven.service.impl;

import com.haven.app.haven.entity.Users;
import com.haven.app.haven.repository.UsersRepository;
import com.haven.app.haven.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;

    @Override
    public Users createUser(Users users) {
        if (emailExists(users.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        return usersRepository.saveAndFlush(users);
    }

    public boolean emailExists(String email) {
        return usersRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByEmail(username);
    }
}
