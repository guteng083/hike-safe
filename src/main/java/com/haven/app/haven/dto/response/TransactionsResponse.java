package com.haven.app.haven.dto.response;

import com.haven.app.haven.entity.Transactions;
import lombok.*;

import java.util.ArrayList;
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
    private Double totalAmount;
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

    public static TransactionsResponse toTransactionResponse(Transactions transactions) {
        List<TicketResponse> ticketResponses = transactions.getTickets() != null
                ? transactions.getTickets().stream()
                .map(ticket -> TicketResponse.builder()
                        .id(ticket.getId())
                        .hikerName(ticket.getHikerName())
                        .address(ticket.getAddress())
                        .phoneNumber(ticket.getPhoneNumber())
                        .build())
                .toList()
                : new ArrayList<>();

        return TransactionsResponse.builder()
                .id(transactions.getId())
                .startDate(transactions.getStartDate().toString())
                .endDate(transactions.getEndDate().toString())
                .status(transactions.getStatus().toString())
                .totalAmount(transactions.getTotalAmount())
                .tickets(ticketResponses)
                .trackerId(transactions.getTrackerId() != null ? transactions.getTrackerId().getId().toString() : null)
                .createdAt(transactions.getCreatedAt().toString())
                .updatedAt(transactions.getUpdatedAt().toString())
                .build();
    }
}
