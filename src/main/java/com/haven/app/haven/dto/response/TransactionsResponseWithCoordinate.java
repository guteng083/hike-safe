package com.haven.app.haven.dto.response;

import com.haven.app.haven.entity.Transactions;
import com.haven.app.haven.service.impl.AuthServiceImpl;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionsResponseWithCoordinate {
    private String id;
    private String startDate;
    private String endDate;
    private String status;
    private Double totalAmount;
    private String paymentUrl;
    private List<TicketResponse> tickets;
    private String trackerId;
    private List<CoordinateResponse> coordinates;
    private String createdAt;
    private LoginResponse users;
    private String updatedAt;

    public static TransactionsResponseWithCoordinate toTransactionResponseWithCoordinates(Transactions transactions) {
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

        return TransactionsResponseWithCoordinate.builder()
                .id(transactions.getId())
                .users(AuthServiceImpl.createLoginResponse(transactions.getUser()))
                .startDate(transactions.getStartDate().toString())
                .endDate(transactions.getEndDate().toString())
                .status(transactions.getStatus().toString())
                .totalAmount(transactions.getTotalAmount())
                .tickets(ticketResponses)
                .paymentUrl(transactions.getPaymentUrl())
                .coordinates(transactions.getCoordinates().stream().map(coordinates1 -> CoordinateResponse.CoordinateToCoordinateResponse(coordinates1)).toList())
                .trackerId(transactions.getTracker() != null ? transactions.getTracker().getId() : null)
                .createdAt(transactions.getCreatedAt().toString())
                .updatedAt(transactions.getUpdatedAt().toString())
                .build();
    }
}
