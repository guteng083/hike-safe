package com.haven.app.haven.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CoordinateResponse {
    private String id;
    private String trackerId;
    private String transactionId;
    private String latitude;
    private String longitude;
    private String updatedAt;
}
