package com.haven.app.haven.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TransactionsResponse {
    private String id;
    private String startDate;
    private String endDate;
    private String status;
    private double amount;
    private List<TicketResponse> tickets;
    private String trackerId;
    private String createdAt;
    private String updatedAt;

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    @Builder
    public static class TicketResponse {
        private String id;
        private String hikerName;
        private String address;
        private String phoneNumber;
    }
}
