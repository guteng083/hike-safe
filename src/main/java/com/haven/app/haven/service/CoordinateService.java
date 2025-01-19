package com.haven.app.haven.service;

import com.haven.app.haven.dto.request.CoordinateRequest;
import com.haven.app.haven.dto.response.CoordinateResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CoordinateService {
    CoordinateResponse addCoordinate(CoordinateRequest coordinateRequest);
    Page<CoordinateResponse> getCoordinate(String transactionId, Integer page, Integer size);
}
