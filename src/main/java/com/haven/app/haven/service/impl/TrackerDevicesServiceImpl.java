package com.haven.app.haven.service.impl;

import com.haven.app.haven.constant.TrackerStatus;
import com.haven.app.haven.dto.request.TrackerDevicesRequest;
import com.haven.app.haven.dto.request.TrackerDevicesStatusRequest;
import com.haven.app.haven.dto.response.TrackerDevicesResponse;
import com.haven.app.haven.entity.TrackerDevices;
import com.haven.app.haven.repository.TrackerDevicesRepository;
import com.haven.app.haven.service.TrackerDevicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackerDevicesServiceImpl implements TrackerDevicesService {
    private final TrackerDevicesRepository trackerDevicesRepository;


    @Override
    public TrackerDevicesResponse createTracker(TrackerDevicesRequest trackerDevicesRequest) {
        TrackerDevices trackerDevices = TrackerDevices.builder()
                .serialNumber(trackerDevicesRequest.getSerialNumber())
                .build();

        trackerDevicesRepository.saveAndFlush(trackerDevices);
        return TrackerDevicesResponse.trackerDevicesToTrackerDevicesResponse(trackerDevices);
    }

    @Override
    public List<TrackerDevicesResponse> getTrackerDevices() {
        List<TrackerDevices> trackerDevices = trackerDevicesRepository.findAll();

        return trackerDevices.stream().map(TrackerDevicesResponse::trackerDevicesToTrackerDevicesResponse).toList();
    }

    @Override
    public TrackerDevicesResponse getTrackerById(String id) {
        TrackerDevices trackerDevices = getOne(id);
        return TrackerDevicesResponse.trackerDevicesToTrackerDevicesResponse(trackerDevices);
    }

    @Override
    public TrackerDevices getOne(String id) {
        return trackerDevicesRepository.findById(id).orElse(null);
    }

    @Override
    public TrackerDevicesResponse updateTracker(String id, TrackerDevicesRequest trackerDevicesRequest) {
        TrackerDevices trackerDevices = getOne(id);

        trackerDevices.setSerialNumber(trackerDevicesRequest.getSerialNumber());
        trackerDevicesRepository.saveAndFlush(trackerDevices);
        return TrackerDevicesResponse.trackerDevicesToTrackerDevicesResponse(trackerDevices);
    }

    @Override
    public TrackerDevicesResponse updateStatus(String id, TrackerDevicesStatusRequest trackerDevicesStatusRequest) {
        TrackerDevices trackerDevices = getOne(id);
        trackerDevices.setStatus(TrackerStatus.getStatus(trackerDevicesStatusRequest.getStatus()));

        trackerDevicesRepository.saveAndFlush(trackerDevices);

        return TrackerDevicesResponse.trackerDevicesToTrackerDevicesResponse(trackerDevices);
    }

    @Override
    public void deleteTracker(String id) {
        TrackerDevices trackerDevices = getOne(id);
        trackerDevicesRepository.delete(trackerDevices);
    }
}
