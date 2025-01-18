package com.haven.app.haven.dto.response;

import com.haven.app.haven.entity.TrackerDevices;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TrackerDevicesResponse {
    private String id;
    private String SerialNumber;
    private String status;

    public static TrackerDevicesResponse trackerDevicesToTrackerDevicesResponse(TrackerDevices trackerDevices) {
        return TrackerDevicesResponse.builder()
                .id(trackerDevices.getId())
                .SerialNumber(trackerDevices.getSerialNumber())
                .status(trackerDevices.getStatus().toString())
                .build();
    }
}
