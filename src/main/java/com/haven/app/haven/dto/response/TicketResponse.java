package com.haven.app.haven.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TicketResponse {
    private String id;
    private String hikerName;
    private String address;
    private String phoneNumber;
}