package com.haven.app.haven.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CoordinateRequest {
    private String transactionId;
    private String latitude;
    private String longitude;
}
