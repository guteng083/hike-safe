package com.haven.app.haven.service.impl;

import com.haven.app.haven.dto.request.CoordinateRequest;
import com.haven.app.haven.dto.response.CoordinateResponse;
import com.haven.app.haven.entity.Coordinates;
import com.haven.app.haven.entity.TrackerDevices;
import com.haven.app.haven.entity.Transactions;
import com.haven.app.haven.exception.CoordinateException;
import com.haven.app.haven.exception.NotFoundException;
import com.haven.app.haven.repository.CoordinateRepository;
import com.haven.app.haven.service.CoordinateService;
import com.haven.app.haven.service.TrackerDevicesService;
import com.haven.app.haven.service.TransactionsService;
import com.haven.app.haven.utils.LogUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        try {
            TrackerDevices trackerDevices = trackerDevicesService.getBySerialNumber(coordinateRequest.getSerialNumber());
            System.out.println(trackerDevices.getSerialNumber());
            Transactions transactions = transactionsService.getTransactionByTracker(trackerDevices);

            Coordinates coordinates = Coordinates.builder()
                .tracker(trackerDevices)
                    .transaction(transactions)
                    .longitude(coordinateRequest.getLongitude())
                    .latitude(coordinateRequest.getLatitude())
                    .build();

            coordinateRepository.saveAndFlush(coordinates);

            LogUtils.logSuccess("CoordinateService", "addCoordinate");

            return CoordinateResponse.CoordinateToCoordinateResponse(coordinates);
        } catch (Exception e) {
            LogUtils.getError("CoordinateService.addCoordinate", e);
            throw new CoordinateException("Failed to add coordinate");
        }
    }

    @Override
    public Page<CoordinateResponse> getCoordinate(String transactionId, Integer page, Integer size) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);

            Page<Coordinates> coordinates = coordinateRepository.findAllByTransaction_Id(pageable, transactionId);

            LogUtils.logSuccess("CoordinateService", "getCoordinate");

            return coordinates.map(CoordinateResponse::CoordinateToCoordinateResponse);
        } catch (Exception e) {
            LogUtils.getError("CoordinateService.getCoordinate", e);
            if(e instanceof NotFoundException) {
                throw e;
            }
            throw new CoordinateException("Failed to get coordinate list");
        }
    }
}
