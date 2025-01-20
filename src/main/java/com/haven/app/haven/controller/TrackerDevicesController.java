package com.haven.app.haven.controller;

import com.haven.app.haven.constant.Constant;
import com.haven.app.haven.dto.request.TrackerDevicesRequest;
import com.haven.app.haven.dto.request.TrackerDevicesStatusRequest;
import com.haven.app.haven.dto.response.CommonResponse;
import com.haven.app.haven.dto.response.CommonResponseWithData;
import com.haven.app.haven.dto.response.PageResponse;
import com.haven.app.haven.dto.response.TrackerDevicesResponse;
import com.haven.app.haven.service.TrackerDevicesService;
import com.haven.app.haven.utils.ResponseUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.TRACKER_DEVICES_API)
@RequiredArgsConstructor
@Validated
@Tag(name="Tracker Device Management", description = "APIs for tracker device management")
public class TrackerDevicesController {
    private final TrackerDevicesService trackerDevicesService;

    @PostMapping
    public CommonResponseWithData<TrackerDevicesResponse> createTrackerDevices(@RequestBody TrackerDevicesRequest trackerDevicesRequest) {
        TrackerDevicesResponse trackerDevicesResponse = trackerDevicesService.createTracker(trackerDevicesRequest);
        return ResponseUtils.responseWithData("Tracker Device Created", trackerDevicesResponse);
    }

    @GetMapping
    public PageResponse<List<TrackerDevicesResponse>> getAllTrackerDevices(
            @Valid
            @NotNull(message = "Page number is required")
            @Min(value = 1, message = "Page number cannot be zero negative")
            @RequestParam(defaultValue = "1") Integer page,

            @Valid
            @NotNull(message = "Page size is required")
            @Min(value = 1, message = "Page size cannot be zero or negative")
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<TrackerDevicesResponse> trackerDevicesResponses = trackerDevicesService.getTrackerDevices(page, size);
        return ResponseUtils.responseWithPage("Tracker Device List", trackerDevicesResponses);
    }

    @GetMapping(path = "/{id}")
    public CommonResponseWithData<TrackerDevicesResponse> getTrackerDevices(@PathVariable String id) {
        TrackerDevicesResponse trackerDevicesResponse = trackerDevicesService.getTrackerById(id);
        return ResponseUtils.responseWithData("Tracker Device Founded", trackerDevicesResponse);
    }

    @PatchMapping(path = "/{id}")
    public CommonResponseWithData<TrackerDevicesResponse> updateTrackerDevices(@PathVariable String id, @RequestBody TrackerDevicesRequest trackerDevicesRequest) {
        TrackerDevicesResponse trackerDevicesResponse = trackerDevicesService.updateTracker(id, trackerDevicesRequest);
        return ResponseUtils.responseWithData("Tracker Device Updated", trackerDevicesResponse);
    }

    @PatchMapping(path = "/{id}/status")
    public CommonResponseWithData<TrackerDevicesResponse> updateStatusTracker(@PathVariable String id, @RequestBody TrackerDevicesStatusRequest trackerDevicesStatusRequest) {
        TrackerDevicesResponse trackerDevicesResponse = trackerDevicesService.updateStatus(id, trackerDevicesStatusRequest);
        return ResponseUtils.responseWithData("Tracker Device Status Updated", trackerDevicesResponse);
    }

    @DeleteMapping(path = "/{id}")
    public CommonResponse deleteTrackerDevices(@PathVariable String id) {
        trackerDevicesService.deleteTracker(id);
        return ResponseUtils.response("Tracker Device has been Deleted");
    }
}
