package com.haven.app.haven.controller;

import com.haven.app.haven.constant.Endpoint;
import com.haven.app.haven.dto.request.SearchRequest;
import com.haven.app.haven.dto.request.UpdateUserRequest;
import com.haven.app.haven.dto.response.*;
import com.haven.app.haven.service.UsersService;
import com.haven.app.haven.utils.ResponseUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Validated
@RequestMapping(path = Endpoint.API_USER)
@RequiredArgsConstructor
@Tag(name="User Management", description = "APIs for user management")
public class UsersController {
    private final UsersService usersService;

    @PatchMapping
    public CommonResponse updateUserDetail(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        usersService.updateUserDetails(updateUserRequest);
        return ResponseUtils.response("Success update user");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/staffs/{id}")
    public CommonResponseWithData<LoginResponse> getStaff(@Valid @PathVariable String id) {
        LoginResponse staff = usersService.getStaffById(id);
        return ResponseUtils.responseWithData("Success Get Staff", staff);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/staffs")
    public PageResponse<List<LoginResponse>> getAllStaff(
            @RequestParam(name = "search", required = false) String search,

            @Valid
            @NotNull(message = "Page number is required")
            @Min(value = 1, message = "Page number cannot be zero negative")
            @RequestParam(defaultValue = "1", name = "page") Integer page,

            @Valid
            @NotNull(message = "Page size is required")
            @Min(value = 1, message = "Page size cannot be zero or negative")
            @RequestParam(defaultValue = "10", name = "size") Integer size) {
        SearchRequest searchRequest = SearchRequest.builder()
                .search(search)
                .page(page)
                .size(size)
                .build();
        Page<LoginResponse> staff = usersService.getAllStaff(searchRequest);
        return ResponseUtils.responseWithPage("Success Get Staff", staff);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/customers")
    public PageResponse<List<LoginResponse>> getAllCustomer(
            @RequestParam(name = "search", required = false) String search,
            @Valid
            @NotNull(message = "Page number is required")
            @Min(value = 1, message = "Page number cannot be zero negative")
            @RequestParam(defaultValue = "1", name = "page") Integer page,

            @Valid
            @NotNull(message = "Page size is required")
            @Min(value = 1, message = "Page size cannot be zero or negative")
            @RequestParam(defaultValue = "10", name = "size") Integer size
    ) {
        SearchRequest searchRequest = SearchRequest.builder()
                .search(search)
                .page(page)
                .size(size)
                .build();

        Page<LoginResponse> staff = usersService.getAllCustomer(searchRequest);
        return ResponseUtils.responseWithPage("Success Get Customer", staff);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/customers/{customerId}")
    public CommonResponseWithData<LoginResponse> getCustomer(@Valid @PathVariable String customerId) {
        LoginResponse customer = usersService.getCustomerById(customerId);
        return ResponseUtils.responseWithData("Success Get Customer", customer);
    }

    @PutMapping("/update/image")
    public CommonResponse updateUserImage(
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        usersService.updateUserImage(image);
        return ResponseUtils.response("Success update user image");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/staffs/{staffId}/reset-password")
    public CommonResponse resetPassword(@PathVariable String staffId) {
        usersService.resetPassword(staffId);
        return ResponseUtils.response("Success reset password");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/staffs/{staffId}")
    public CommonResponse deleteStaff(@PathVariable String staffId) {
        usersService.deleteStaff(staffId);
        return ResponseUtils.response("Success delete staff");
    }
}
