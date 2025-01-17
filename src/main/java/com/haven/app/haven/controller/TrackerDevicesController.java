package com.haven.app.haven.controller;

import com.haven.app.haven.constant.Constant;
import com.haven.app.haven.dto.request.TrackerDevicesRequest;
import com.haven.app.haven.dto.request.TrackerDevicesStatusRequest;
import com.haven.app.haven.dto.response.TrackerDevicesResponse;
import com.haven.app.haven.service.TrackerDevicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.TRACKER_DEVICES_API)
@RequiredArgsConstructor
public class TrackerDevicesController {
    private final TrackerDevicesService trackerDevicesService;

    @PostMapping
    public ResponseEntity<?> createTrackerDevices(@RequestBody TrackerDevicesRequest trackerDevicesRequest) {
        TrackerDevicesResponse trackerDevicesResponse = trackerDevicesService.createTracker(trackerDevicesRequest);
        return ResponseEntity.ok(trackerDevicesResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllTrackerDevices() {
        List<TrackerDevicesResponse> trackerDevicesResponses = trackerDevicesService.getTrackerDevices();
        return ResponseEntity.ok(trackerDevicesResponses);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getTrackerDevices(@PathVariable String id) {
        TrackerDevicesResponse trackerDevicesResponse = trackerDevicesService.getTrackerById(id);
        return ResponseEntity.ok(trackerDevicesResponse);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> updateTrackerDevices(@PathVariable String id, @RequestBody TrackerDevicesRequest trackerDevicesRequest) {
        TrackerDevicesResponse trackerDevicesResponse = trackerDevicesService.updateTracker(id, trackerDevicesRequest);
        return ResponseEntity.ok(trackerDevicesResponse);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> updateStatusTracker(@PathVariable String id, @RequestBody TrackerDevicesStatusRequest trackerDevicesStatusRequest) {
        TrackerDevicesResponse trackerDevicesResponse = trackerDevicesService.updateStatus(id, trackerDevicesStatusRequest);
        return ResponseEntity.ok(trackerDevicesResponse);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteTrackerDevices(@PathVariable String id) {
        trackerDevicesService.deleteTracker(id);
        return ResponseEntity.ok().build();
    }
}
