package com.haven.app.haven.dto.response;

import com.haven.app.haven.entity.Coordinate;
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

    public static CoordinateResponse CoordinateToCoordinateResponse(Coordinate coordinate) {
        return CoordinateResponse.builder()
                .id(coordinate.getId().toString())
                .trackerId(coordinate.getTrackerId().toString())
                .transactionId(coordinate.getTransactionId().toString())
                .latitude(coordinate.getLatitude())
                .longitude(coordinate.getLongitude())
                .updatedAt(coordinate.getUpdatedAt().toString())
                .build();
    }
}
