package com.haven.app.haven.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class GetCoordinateRequest {
    private String TransactionId;
}
