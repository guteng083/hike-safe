package com.haven.app.haven.service;

import com.haven.app.haven.dto.request.TrackerDevicesRequest;
import com.haven.app.haven.dto.response.TrackerDevicesResponse;
import com.haven.app.haven.entity.TrackerDevices;

public interface TrackerDevicesService {
    TrackerDevicesResponse createTracker(TrackerDevicesRequest trackerDevicesRequest);
    TrackerDevicesResponse getTrackerById(TrackerDevicesRequest trackerDevicesRequest);
    TrackerDevices getOne(TrackerDevicesRequest trackerDevicesRequest);
    TrackerDevicesResponse updateTracker(TrackerDevicesRequest trackerDevicesRequest);
    void deleteTracker(TrackerDevicesRequest trackerDevicesRequest);
}
