package com.haven.app.haven.service.impl;

import com.haven.app.haven.dto.request.CoordinateRequest;
import com.haven.app.haven.dto.response.CoordinateResponse;
import com.haven.app.haven.entity.Coordinates;
import com.haven.app.haven.entity.TrackerDevices;
import com.haven.app.haven.entity.Transactions;
import com.haven.app.haven.repository.CoordinateRepository;
import com.haven.app.haven.service.CoordinateService;
import com.haven.app.haven.service.TrackerDevicesService;
import com.haven.app.haven.service.TransactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoordinateServiceImpl implements CoordinateService {
    private final CoordinateRepository coordinateRepository;
    private final TrackerDevicesService trackerDevicesService;
    private final TransactionsService transactionsService;

    @Override
    public CoordinateResponse addCoordinate(CoordinateRequest coordinateRequest) {
        Transactions transactions = transactionsService.getOne(coordinateRequest.getTransactionId());
        TrackerDevices trackerDevices = trackerDevicesService.getOne(transactions.getTracker().getId());

        Coordinates coordinates = Coordinates.builder()
                .tracker(trackerDevices)
                .transaction(transactions)
                .longitude(coordinateRequest.getLongitude())
                .latitude(coordinateRequest.getLatitude())
                .build();

        coordinateRepository.saveAndFlush(coordinates);

        return CoordinateResponse.CoordinateToCoordinateResponse(coordinates);
    }

    @Override
    public List<CoordinateResponse> getCoordinate(String transactionId) {
        Transactions transactions = transactionsService.getOne(transactionId);

        return transactions.getCoordinates().stream()
                .map(CoordinateResponse::CoordinateToCoordinateResponse).toList();
    }

}
