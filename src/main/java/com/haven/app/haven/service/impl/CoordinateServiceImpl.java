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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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

            log.info("Coordinate Service: Coordinate successfully added");

            return CoordinateResponse.CoordinateToCoordinateResponse(coordinates);
        } catch (Exception e) {
            getError(e);
            throw new CoordinateException("Failed to add coordinate");
        }
    }

    @Override
    public Page<CoordinateResponse> getCoordinate(String transactionId, Integer page, Integer size) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);

            Page<Coordinates> coordinates = coordinateRepository.findAllByTransaction_Id(pageable, transactionId);

            log.info("Coordinate Service: Get coordinate list successfully");

            return coordinates.map(CoordinateResponse::CoordinateToCoordinateResponse);
        } catch (Exception e) {
            getError(e);
            if(e instanceof NotFoundException) {
                throw e;
            }
            throw new CoordinateException("Failed to get coordinate list");
        }
    }

    private static void getError(Exception e) {
        log.error("Error Coordinate Service:{}", e.getMessage());
    }

}
