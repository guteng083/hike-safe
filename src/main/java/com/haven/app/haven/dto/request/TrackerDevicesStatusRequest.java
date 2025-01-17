package com.haven.app.haven.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackerDevicesStatusRequest {
    private String status;
}
