package com.haven.app.haven.controller;

import com.haven.app.haven.constant.Constant;
import com.haven.app.haven.dto.request.CoordinateRequest;
import com.haven.app.haven.dto.response.CoordinateResponse;
import com.haven.app.haven.service.CoordinateService;
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
    public ResponseEntity<?> addCoordinate(@RequestBody CoordinateRequest coordinateRequest) {
        CoordinateResponse coordinateResponse = coordinateService.addCoordinate(coordinateRequest);
        return ResponseEntity.ok(coordinateResponse);
    }

    @GetMapping(path = "/{transactionId}")
    public ResponseEntity<?> getCoordinate(@PathVariable String transactionId) {
        List<CoordinateResponse> coordinateResponse = coordinateService.getCoordinate(transactionId);
        return ResponseEntity.ok(coordinateResponse);
    }

}
