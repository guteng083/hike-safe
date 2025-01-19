package com.haven.app.haven.service;

import com.haven.app.haven.dto.request.TrackerDevicesRequest;
import com.haven.app.haven.dto.request.TrackerDevicesStatusRequest;
import com.haven.app.haven.dto.response.TrackerDevicesResponse;
import com.haven.app.haven.entity.TrackerDevices;

import java.util.List;

public interface TrackerDevicesService {
    TrackerDevicesResponse createTracker(TrackerDevicesRequest trackerDevicesRequest);
    List<TrackerDevicesResponse> getTrackerDevices();
    TrackerDevicesResponse getTrackerById(String id);
    TrackerDevices getOne(String id);
    TrackerDevicesResponse updateTracker(String id, TrackerDevicesRequest trackerDevicesRequest);
    TrackerDevicesResponse updateStatus(String id, TrackerDevicesStatusRequest trackerDevicesStatusRequest);
    void deleteTracker(String id);
    TrackerDevices getBySerialNumber(String serialNumber);
}
