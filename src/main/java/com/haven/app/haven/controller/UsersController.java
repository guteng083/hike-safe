package com.haven.app.haven.controller;

import com.haven.app.haven.constant.Endpoint;
import com.haven.app.haven.dto.request.UpdateUserRequest;
import com.haven.app.haven.dto.response.CommonResponse;
import com.haven.app.haven.service.UsersService;
import com.haven.app.haven.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping(path = Endpoint.API_USER)
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    @PatchMapping
    public CommonResponse updateUserDetail(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        usersService.updateUserDetails(updateUserRequest);
        return ResponseUtils.Response("Success update user");
    }
}
