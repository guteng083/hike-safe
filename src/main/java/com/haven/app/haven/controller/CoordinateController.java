package com.haven.app.haven.controller;

import com.haven.app.haven.constant.Constant;
import com.haven.app.haven.dto.request.CoordinateRequest;
import com.haven.app.haven.dto.response.CommonResponseWithData;
import com.haven.app.haven.dto.response.CoordinateResponse;
import com.haven.app.haven.service.CoordinateService;
import com.haven.app.haven.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.COORDINATE_API)
@RequiredArgsConstructor
public class CoordinateController {
    private final CoordinateService coordinateService;

    @PostMapping
    public CommonResponseWithData<CoordinateResponse> addCoordinate(@RequestBody CoordinateRequest coordinateRequest) {
        CoordinateResponse coordinateResponse = coordinateService.addCoordinate(coordinateRequest);
        return ResponseUtils.responseWithData("Coordinate added", coordinateResponse);
    }

    @GetMapping(path = "/{transactionId}")
    public CommonResponseWithData<List<CoordinateResponse>> getCoordinate(@PathVariable String transactionId) {
        List<CoordinateResponse> coordinateResponse = coordinateService.getCoordinate(transactionId);
        return ResponseUtils.responseWithData("Coordinate list", coordinateResponse);
    }

}
