package com.haven.app.haven.controller;

import com.haven.app.haven.constant.Constant;
import com.haven.app.haven.dto.request.TrackerDevicesRequest;
import com.haven.app.haven.dto.request.TrackerDevicesStatusRequest;
import com.haven.app.haven.dto.response.CommonResponse;
import com.haven.app.haven.dto.response.CommonResponseWithData;
import com.haven.app.haven.dto.response.TrackerDevicesResponse;
import com.haven.app.haven.service.TrackerDevicesService;
import com.haven.app.haven.utils.ResponseUtils;
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
    public CommonResponseWithData<TrackerDevicesResponse> createTrackerDevices(@RequestBody TrackerDevicesRequest trackerDevicesRequest) {
        TrackerDevicesResponse trackerDevicesResponse = trackerDevicesService.createTracker(trackerDevicesRequest);
        return ResponseUtils.ResponseWithData("Tracker Device Created", trackerDevicesResponse);
    }

    @GetMapping
    public CommonResponseWithData<List<TrackerDevicesResponse>> getAllTrackerDevices() {
        List<TrackerDevicesResponse> trackerDevicesResponses = trackerDevicesService.getTrackerDevices();
        return ResponseUtils.ResponseWithData("Tracker Device List", trackerDevicesResponses);
    }

    @GetMapping(path = "/{id}")
    public CommonResponseWithData<TrackerDevicesResponse> getTrackerDevices(@PathVariable String id) {
        TrackerDevicesResponse trackerDevicesResponse = trackerDevicesService.getTrackerById(id);
        return ResponseUtils.ResponseWithData("Tracker Device Founded", trackerDevicesResponse);
    }

    @PatchMapping(path = "/{id}")
    public CommonResponseWithData<TrackerDevicesResponse> updateTrackerDevices(@PathVariable String id, @RequestBody TrackerDevicesRequest trackerDevicesRequest) {
        TrackerDevicesResponse trackerDevicesResponse = trackerDevicesService.updateTracker(id, trackerDevicesRequest);
        return ResponseUtils.ResponseWithData("Tracker Device Updated", trackerDevicesResponse);
    }

    @PatchMapping(path = "/{id}/status")
    public CommonResponseWithData<TrackerDevicesResponse> updateStatusTracker(@PathVariable String id, @RequestBody TrackerDevicesStatusRequest trackerDevicesStatusRequest) {
        TrackerDevicesResponse trackerDevicesResponse = trackerDevicesService.updateStatus(id, trackerDevicesStatusRequest);
        return ResponseUtils.ResponseWithData("Tracker Device Status Updated", trackerDevicesResponse);
    }

    @DeleteMapping(path = "/{id}")
    public CommonResponse deleteTrackerDevices(@PathVariable String id) {
        trackerDevicesService.deleteTracker(id);
        return ResponseUtils.Response("Tracker Device has been Deleted");
    }
}
