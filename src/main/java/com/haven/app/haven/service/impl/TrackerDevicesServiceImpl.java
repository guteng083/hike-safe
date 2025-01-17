package com.haven.app.haven.service.impl;

import com.haven.app.haven.dto.request.TrackerDevicesRequest;
import com.haven.app.haven.dto.response.TrackerDevicesResponse;
import com.haven.app.haven.entity.TrackerDevices;
import com.haven.app.haven.repository.TrackerDevicesRepository;
import com.haven.app.haven.service.TrackerDevicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackerDevicesServiceImpl implements TrackerDevicesService {
    private final TrackerDevicesRepository trackerDevicesRepository;

    @Override
    public TrackerDevicesResponse createTracker(TrackerDevicesRequest trackerDevicesRequest) {
        return null;
    }

    @Override
    public TrackerDevicesResponse getTrackerById(TrackerDevicesRequest trackerDevicesRequest) {
        return null;
    }

    @Override
    public TrackerDevices getOne(TrackerDevicesRequest trackerDevicesRequest) {
        return null;
    }

    @Override
    public TrackerDevicesResponse updateTracker(TrackerDevicesRequest trackerDevicesRequest) {
        return null;
    }

    @Override
    public void deleteTracker(TrackerDevicesRequest trackerDevicesRequest) {

    }
}
