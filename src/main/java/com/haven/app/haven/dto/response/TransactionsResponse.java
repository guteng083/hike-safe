package com.haven.app.haven.dto.response;

import com.haven.app.haven.entity.Transactions;
import com.haven.app.haven.service.impl.AuthServiceImpl;
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
    private LoginResponse user;
    private String startDate;
    private String endDate;
    private String status;
    private String paymentUrl;
    private Double totalAmount;
    private List<TicketResponse> tickets;
    private TrackerDevicesResponse trackerDevices;
    private List<CoordinateResponse> coordinates;
    private String createdAt;
    private String updatedAt;

    public static TransactionsResponse toTransactionResponse(Transactions transactions) {
        List<TicketResponse> ticketResponses = transactions.getTickets() != null
                ? transactions.getTickets().stream()
                .map(ticket -> TicketResponse.builder()
                        .id(ticket.getId())
                        .hikerName(ticket.getHikerName())
                        .address(ticket.getAddress())
                        .phoneNumber(ticket.getPhoneNumber())
                        .ticketPrice(ticket.getPrices().getPrice())
                        .ticketType(ticket.getPrices().getPriceType())
                        .build())
                .toList()
                : new ArrayList<>();

        return TransactionsResponse.builder()
                .id(transactions.getId())
                .user(AuthServiceImpl.createLoginResponse(transactions.getUser()))
                .startDate(transactions.getStartDate().toString())
                .endDate(transactions.getEndDate().toString())
                .status(transactions.getStatus().toString())
                .totalAmount(transactions.getTotalAmount())
                .paymentUrl(transactions.getPaymentUrl())
                .tickets(ticketResponses)
                .trackerDevices(transactions.getTracker() != null ? TrackerDevicesResponse.trackerDevicesToTrackerDevicesResponse(transactions.getTracker()) : null)
                .coordinates(transactions.getCoordinates().stream().map(coordinates1 -> CoordinateResponse.CoordinateToCoordinateResponse(coordinates1)).toList())
                .createdAt(transactions.getCreatedAt().toString())
                .updatedAt(transactions.getUpdatedAt().toString())
                .build();
    }

    public static TransactionsResponse toTransactionResponseWithoutTrackerDevice(Transactions transactions) {
        List<TicketResponse> ticketResponses = transactions.getTickets() != null
                ? transactions.getTickets().stream()
                .map(ticket -> TicketResponse.builder()
                        .id(ticket.getId())
                        .hikerName(ticket.getHikerName())
                        .address(ticket.getAddress())
                        .phoneNumber(ticket.getPhoneNumber())
                        .ticketPrice(ticket.getPrices().getPrice())
                        .ticketType(ticket.getPrices().getPriceType())
                        .build())
                .toList()
                : new ArrayList<>();

        return TransactionsResponse.builder()
                .id(transactions.getId())
                .user(AuthServiceImpl.createLoginResponse(transactions.getUser()))
                .startDate(transactions.getStartDate().toString())
                .endDate(transactions.getEndDate().toString())
                .status(transactions.getStatus().toString())
                .totalAmount(transactions.getTotalAmount())
                .paymentUrl(transactions.getPaymentUrl())
                .tickets(ticketResponses)
                .coordinates(transactions.getCoordinates().stream().map(coordinates1 -> CoordinateResponse.CoordinateToCoordinateResponse(coordinates1)).toList())
                .createdAt(transactions.getCreatedAt().toString())
                .updatedAt(transactions.getUpdatedAt().toString())
                .build();
    }
}
