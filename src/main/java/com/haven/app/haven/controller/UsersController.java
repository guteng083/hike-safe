package com.haven.app.haven.controller;

import com.haven.app.haven.constant.Endpoint;
import com.haven.app.haven.dto.request.UpdateUserRequest;
import com.haven.app.haven.dto.response.CommonResponse;
import com.haven.app.haven.dto.response.CommonResponseWithData;
import com.haven.app.haven.dto.response.LoginResponse;
import com.haven.app.haven.dto.response.PageResponse;
import com.haven.app.haven.service.UsersService;
import com.haven.app.haven.utils.ResponseUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping(path = Endpoint.API_USER)
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    @PatchMapping
    public CommonResponse updateUserDetail(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        usersService.updateUserDetails(updateUserRequest);
        return ResponseUtils.response("Success update user");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public CommonResponseWithData<LoginResponse> getStaff(@Valid @PathVariable String id) {
        LoginResponse staff = usersService.getStaffById(id);
        return ResponseUtils.responseWithData("Success Get Staff", staff);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public PageResponse<List<LoginResponse>> getAllStaff(
            @Valid
            @NotNull(message = "Page number is required")
            @Min(value = 1, message = "Page number cannot be zero negative")
            @RequestParam Integer page,

            @Valid
            @NotNull(message = "Page size is required")
            @Min(value = 1, message = "Page size cannot be zero or negative")
            @RequestParam Integer size)
    {
        Page<LoginResponse> staff = usersService.getAllStaff(page, size);
        return ResponseUtils.responseWithPage("Success Get Staff", staff);
    }
}
