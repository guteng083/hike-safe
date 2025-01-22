package com.haven.app.haven.dto.response;

import com.haven.app.haven.constant.PriceType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TicketResponse {
    private String id;
    private String hikerName;
    private Double ticketPrice;
    private PriceType ticketType;
    private String address;
    private String phoneNumber;
}