package com.haven.app.haven.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TrackerDevicesResponse {
    private String SerialNumber;
    private String status;
}
