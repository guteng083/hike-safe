package com.haven.app.haven.service;

import com.haven.app.haven.dto.request.CoordinateRequest;
import com.haven.app.haven.dto.request.GetCoordinateRequest;
import com.haven.app.haven.dto.response.CoordinateResponse;

public interface CoordinateService {
    CoordinateResponse addCoordinate(CoordinateRequest coordinateRequest);
    CoordinateResponse getCoordinate(GetCoordinateRequest getCoordinateRequest);
}
