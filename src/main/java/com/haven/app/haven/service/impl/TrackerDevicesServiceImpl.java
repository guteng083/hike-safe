package com.haven.app.haven.service.impl;

import com.haven.app.haven.constant.TrackerStatus;
import com.haven.app.haven.dto.request.TrackerDevicesRequest;
import com.haven.app.haven.dto.request.TrackerDevicesStatusRequest;
import com.haven.app.haven.dto.response.TrackerDevicesResponse;
import com.haven.app.haven.entity.TrackerDevices;
import com.haven.app.haven.exception.NotFoundException;
import com.haven.app.haven.exception.TrackerDeviceException;
import com.haven.app.haven.repository.TrackerDevicesRepository;
import com.haven.app.haven.service.TrackerDevicesService;
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
public class TrackerDevicesServiceImpl implements TrackerDevicesService {
    private final TrackerDevicesRepository trackerDevicesRepository;


    @Override
    public TrackerDevicesResponse createTracker(TrackerDevicesRequest trackerDevicesRequest) {
        try {
            TrackerDevices trackerDevices = TrackerDevices.builder()
                    .serialNumber(trackerDevicesRequest.getSerialNumber())
                    .build();

            trackerDevicesRepository.saveAndFlush(trackerDevices);

            log.info("Tracker Device Service: Tracker device created successfully");

            return TrackerDevicesResponse.trackerDevicesToTrackerDevicesResponse(trackerDevices);
        } catch (Exception e) {
            getError(e);
            throw new TrackerDeviceException("Failed to create tracker device");
        }
    }

    @Override
    public Page<TrackerDevicesResponse> getTrackerDevices(Integer page, Integer size) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<TrackerDevices> trackerDevices = trackerDevicesRepository.findAll(pageable);

            if (trackerDevices.isEmpty()){
                throw new NotFoundException("Tracker device list not found");
            }

            log.info("Tracker Device Service: Get tracker devices list");

            return trackerDevices.map(TrackerDevicesResponse::trackerDevicesToTrackerDevicesResponse);
        } catch (Exception e) {
            getError(e);
            if (e instanceof NotFoundException){
                throw e;
            }
            throw new TrackerDeviceException("Failed to get tracker device list");
        }
    }

    @Override
    public TrackerDevicesResponse getTrackerById(String id) {
        try {
            TrackerDevices trackerDevices = getOne(id);

            log.info("Tracker Device Service: Get tracker device");

            return TrackerDevicesResponse.trackerDevicesToTrackerDevicesResponse(trackerDevices);
        } catch (Exception e) {
            getError(e);
            if(e instanceof NotFoundException) {
                throw e;
            }
            throw new TrackerDeviceException("Failed to get tracker device");
        }
    }

    @Override
    public TrackerDevices getOne(String id) {
        try {
            return trackerDevicesRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Tracker device not found"));
        } catch (Exception e) {
            getError(e);
            if(e instanceof NotFoundException) {
                throw e;
            }
            throw new TrackerDeviceException("Failed to get tracker device");
        }
    }

    @Override
    public TrackerDevicesResponse updateTracker(String id, TrackerDevicesRequest trackerDevicesRequest) {
        try {
            TrackerDevices trackerDevices = getOne(id);

            trackerDevices.setSerialNumber(trackerDevicesRequest.getSerialNumber());
            trackerDevicesRepository.saveAndFlush(trackerDevices);

            log.info("Tracker Device Service: Tracker device updated successfully");

            return TrackerDevicesResponse.trackerDevicesToTrackerDevicesResponse(trackerDevices);
        } catch (Exception e) {
            getError(e);
            if(e instanceof NotFoundException) {
                throw e;
            }
            throw new TrackerDeviceException("Failed to update tracker device");
        }
    }

    @Override
    public TrackerDevicesResponse updateStatus(String id, TrackerDevicesStatusRequest trackerDevicesStatusRequest) {
        try {
            TrackerDevices trackerDevices = getOne(id);
            trackerDevices.setStatus(TrackerStatus.getStatus(trackerDevicesStatusRequest.getStatus()));

            trackerDevicesRepository.saveAndFlush(trackerDevices);

            log.info("Tracker Device Service: Tracker device status updated successfully");

            return TrackerDevicesResponse.trackerDevicesToTrackerDevicesResponse(trackerDevices);
        } catch (Exception e) {
            getError(e);
            if(e instanceof NotFoundException) {
                throw e;
            }
            throw new TrackerDeviceException("Failed to update tracker device status");
        }
    }

    @Override
    public void deleteTracker(String id) {
        try {
            TrackerDevices trackerDevices = getOne(id);
            trackerDevicesRepository.delete(trackerDevices);

            log.info("Tracker Device Service: Tracker device deleted successfully");
        } catch (Exception e) {
            getError(e);
            if(e instanceof NotFoundException) {
                throw e;
            }
            throw new TrackerDeviceException("Failed to delete tracker device");
        }
    }

    @Override
    public TrackerDevices getBySerialNumber(String serialNumber) {
        try {
            TrackerDevices trackerDevices = trackerDevicesRepository.findBySerialNumber(serialNumber);

            if(trackerDevices == null) {
                throw new NotFoundException("Tracker device not found");
            }

            log.info("Tracker Device Service: Get tracker device by serial number");

            return trackerDevices;
        } catch (Exception e) {
            getError(e);
            throw new NotFoundException("Tracker device not found");
        }
    }

    private static void getError(Exception e) {
        log.error("Error Tracker Devices Service:{}", e.getMessage());
    }
}
