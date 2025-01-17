package com.haven.app.haven.service;

import com.haven.app.haven.dto.request.CoordinateRequest;
import com.haven.app.haven.dto.response.CoordinateResponse;

import java.util.List;

public interface CoordinateService {
    CoordinateResponse addCoordinate(CoordinateRequest coordinateRequest);
    List<CoordinateResponse> getCoordinate(String transactionId);
}
