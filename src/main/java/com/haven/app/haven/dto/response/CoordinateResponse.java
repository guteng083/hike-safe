package com.haven.app.haven.dto.response;

import com.haven.app.haven.entity.Coordinates;
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

    public static CoordinateResponse CoordinateToCoordinateResponse(Coordinates coordinates) {
        return CoordinateResponse.builder()
                .id(coordinates.getId())
                .trackerId(coordinates.getTracker().getId())
                .transactionId(coordinates.getTransaction().getId())
                .latitude(coordinates.getLatitude())
                .longitude(coordinates.getLongitude())
                .updatedAt(coordinates.getUpdatedAt().toString())
                .build();
    }
}
