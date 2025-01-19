package com.haven.app.haven.service.impl;

import com.haven.app.haven.constant.PriceType;
import com.haven.app.haven.constant.TrackerStatus;
import com.haven.app.haven.constant.TransactionStatus;
import com.haven.app.haven.dto.request.TransactionsRequest;
import com.haven.app.haven.dto.request.TransactionsStatusRequest;
import com.haven.app.haven.dto.response.CommonResponse;
import com.haven.app.haven.dto.response.CommonResponseWithData;
import com.haven.app.haven.dto.response.TransactionsResponse;
import com.haven.app.haven.entity.*;
import com.haven.app.haven.repository.*;
import com.haven.app.haven.service.TrackerDevicesService;
import com.haven.app.haven.service.TransactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransactionsServiceImpl implements TransactionsService {
    private final TransactionsRepository transactionsRepository;
    private final TicketRepository ticketRepository;
    private final UsersRepository usersRepository;
    private final PricesRepository pricesRepository;
    private final TrackerDevicesService trackerDevicesService;
    private final TrackerDevicesRepository trackerDevicesRepository;

    @Override
    public TransactionsResponse createTransaction(TransactionsRequest request) {
        Users user = usersRepository.findById(request.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        LocalDate startDate = LocalDate.parse(request.getStartDate());
        LocalDate endDate = LocalDate.parse(request.getEndDate());
        if(endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        Transactions transactions = Transactions.builder()
                .user(user)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        transactionsRepository.saveAndFlush(transactions);

        List<Tickets> tickets = new ArrayList<>();
        Double totalAmount = 0.0;

        for(TransactionsRequest.TicketRequest ticketRequest : request.getTickets()) {
            Prices prices = pricesRepository.findByPriceType(PriceType.getPriceType(ticketRequest.getIdentificationType()));

            if(prices == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price type not found");
            }

            if(Objects.isNull(ticketRequest.getHikerName()) || ticketRequest.getHikerName().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hiker name is required");
            }

            Tickets ticket = Tickets.builder()
                    .transaction(transactions)
                    .hikerName(ticketRequest.getHikerName())
                    .identificationNumber(ticketRequest.getIdentificationNumber())
                    .address(ticketRequest.getAddress())
                    .phoneNumber(ticketRequest.getPhoneNumber())
                    .prices(prices)
                    .build();
            tickets.add(ticket);
            totalAmount += prices.getPrice();
        }

        ticketRepository.saveAll(tickets);

        transactions.setTotalAmount(totalAmount);

        transactions = transactionsRepository.saveAndFlush(transactions);

        return TransactionsResponse.toTransactionResponse(transactions);
    }

    @Override
    public List<TransactionsResponse> getTransactions() {
        List<Transactions> transactions = transactionsRepository.findAll();

        return transactions.stream().map(TransactionsResponse::toTransactionResponse).toList();
    }

    @Override
    public TransactionsResponse updateTransactionStatus(String id, TransactionsStatusRequest request) {
        Transactions transactions = getOne(id);

        transactions.setStatus(TransactionStatus.fromValue(request.getStatus()));
        transactionsRepository.saveAndFlush(transactions);

        return TransactionsResponse.toTransactionResponse(transactions);
    }

    @Override
    public Transactions getOne(String id) {
        return transactionsRepository.findById(id).orElse(null);
    }

    @Override
    public void deviceAssignment(String id, String deviceId) {
        Transactions transactions = getOne(id);
        TrackerDevices trackerDevices = trackerDevicesService.getOne(deviceId);

        transactions.setTracker(trackerDevices);

        trackerDevices.setStatus(TrackerStatus.USED);

        trackerDevicesRepository.saveAndFlush(trackerDevices);

        transactionsRepository.saveAndFlush(transactions);
    }

    @Override
    public Transactions getTransactionByTracker(TrackerDevices trackerDevices) {
        return transactionsRepository.findByTracker(trackerDevices);
    }
}
