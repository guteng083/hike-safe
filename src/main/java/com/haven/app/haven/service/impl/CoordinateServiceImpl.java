package com.haven.app.haven.service.impl;

import com.haven.app.haven.dto.request.CoordinateRequest;
import com.haven.app.haven.dto.response.CoordinateResponse;
import com.haven.app.haven.entity.Coordinate;
import com.haven.app.haven.entity.TrackerDevices;
import com.haven.app.haven.repository.CoordinateRepository;
import com.haven.app.haven.service.CoordinateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoordinateServiceImpl implements CoordinateService {
    private final CoordinateRepository coordinateRepository;
    private final TrackerDevicesServiceImpl trackerDevicesServiceImpl;

    @Override
    public CoordinateResponse addCoordinate(CoordinateRequest coordinateRequest) {
        TrackerDevices trackerDevices = trackerDevicesServiceImpl.getOne(coordinateRequest.getTrackerId());

//        Minus add transaction
        Coordinate coordinate = Coordinate.builder()
                .trackerId(trackerDevices)
                .longitude(coordinateRequest.getLongitude())
                .latitude(coordinateRequest.getLatitude())
                .build();

        coordinateRepository.saveAndFlush(coordinate);

        return CoordinateResponse.CoordinateToCoordinateResponse(coordinate);
    }

    @Override
    public List<CoordinateResponse> getCoordinate(String transactionId) {
        List<Coordinate> coordinates;
    }
}
