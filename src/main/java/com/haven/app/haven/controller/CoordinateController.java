package com.haven.app.haven.controller;

import com.haven.app.haven.constant.Constant;
import com.haven.app.haven.dto.request.CoordinateRequest;
import com.haven.app.haven.dto.response.CommonResponseWithData;
import com.haven.app.haven.dto.response.CoordinateResponse;
import com.haven.app.haven.dto.response.PageResponse;
import com.haven.app.haven.service.CoordinateService;
import com.haven.app.haven.utils.ResponseUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.COORDINATE_API)
@RequiredArgsConstructor
@Validated
@Tag(name="Coordinates Management", description = "APIs for authentication coordinates management")
public class CoordinateController {
    private final CoordinateService coordinateService;

    @SecurityRequirements
    @PostMapping
    public CommonResponseWithData<CoordinateResponse> addCoordinate(@RequestBody CoordinateRequest coordinateRequest) {
        CoordinateResponse coordinateResponse = coordinateService.addCoordinate(coordinateRequest);
        return ResponseUtils.responseWithData("Coordinate added", coordinateResponse);
    }

    @SecurityRequirements
    @GetMapping(path = "/{transactionId}")
    public PageResponse<List<CoordinateResponse>> getCoordinate(
            @PathVariable String transactionId,
            @Valid
            @NotNull(message = "Page number is required")
            @Min(value = 1, message = "Page number cannot be zero negative")
            @RequestParam(defaultValue = "1") Integer page,

            @Valid
            @NotNull(message = "Page size is required")
            @Min(value = 1, message = "Page size cannot be zero or negative")
            @RequestParam(defaultValue = "10") Integer size)
    {
        Page<CoordinateResponse> coordinateResponse = coordinateService.getCoordinate(transactionId, page, size);
        return ResponseUtils.responseWithPage("Coordinate list", coordinateResponse);
    }

}
