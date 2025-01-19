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
import com.haven.app.haven.exception.NotFoundException;
import com.haven.app.haven.exception.TrackerDeviceException;
import com.haven.app.haven.exception.TransactionsException;
import com.haven.app.haven.repository.*;
import com.haven.app.haven.service.TrackerDevicesService;
import com.haven.app.haven.service.TransactionsService;
import com.haven.app.haven.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionsServiceImpl implements TransactionsService {
    private final TransactionsRepository transactionsRepository;
    private final TicketRepository ticketRepository;
    private final UsersService usersService;
    private final PricesRepository pricesRepository;
    private final TrackerDevicesService trackerDevicesService;
    private final TrackerDevicesRepository trackerDevicesRepository;

    @Override
    public TransactionsResponse createTransaction(TransactionsRequest request) {
        try {
            Users user = usersService.getMe();

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
                    throw new NotFoundException("Prices not found");
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

            transactions.setTickets(tickets);

            ticketRepository.saveAllAndFlush(tickets);

            transactions.setTotalAmount(totalAmount);

            transactions = transactionsRepository.saveAndFlush(transactions);

            log.info("Transactions Service: Transactions created successfully");

            return TransactionsResponse.toTransactionResponse(transactions);
        } catch (Exception e) {
            getError(e);
            if (e instanceof NotFoundException) {
                throw e;
            }
            throw new TransactionsException("Failed to create transaction");
        }
    }

    @Override
    public Page<TransactionsResponse> getTransactions(Integer page, Integer size) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Transactions> transactions = transactionsRepository.findAll(pageable);

            if(transactions.isEmpty()) {
                throw new NotFoundException("Transactions not found");
            }

            log.info("Transactions Service: Get transactions list successfully");

            return transactions.map(TransactionsResponse::toTransactionResponse);
        } catch (Exception e) {
            getError(e);
            if (e instanceof NotFoundException) {
                throw e;
            }
            throw new TransactionsException("Failed to get transactions list");
        }
    }

    @Override
    public TransactionsResponse updateTransactionStatus(String id, TransactionsStatusRequest request) {
        try {
            Transactions transactions = getOne(id);

            transactions.setStatus(TransactionStatus.fromValue(request.getStatus()));
            transactions = transactionsRepository.saveAndFlush(transactions);

            if (transactions.getStatus() == TransactionStatus.DONE) {
                TrackerDevices trackerDevices = transactions.getTracker();
                trackerDevices.setStatus(TrackerStatus.NOT_USED);
                trackerDevicesRepository.saveAndFlush(trackerDevices);
            }

            log.info("Transactions Service: Transactions updated successfully");

            return TransactionsResponse.toTransactionResponse(transactions);
        } catch (Exception e) {
            getError(e);
            if (e instanceof NotFoundException) {
                throw e;
            }
            throw new TransactionsException("Failed to update transactions");
        }

    }

    @Override
    public Transactions getOne(String id) {
        try {
            return transactionsRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Transactions not found"));
        } catch (Exception e) {
            getError(e);
            if (e instanceof NotFoundException) {
                throw e;
            }
            throw new NotFoundException("Transactions not found");
        }
    }

    @Override
    public void deviceAssignment(String id, String deviceId) {
        try {
            Transactions transactions = getOne(id);

            if((transactions.getStatus() == TransactionStatus.CANCELLED) || (transactions.getStatus() == TransactionStatus.PENDING)) {
                throw new TransactionsException("Transactions have not booked");
            }

            TrackerDevices trackerDevices = trackerDevicesService.getOne(deviceId);

            if(trackerDevices.getStatus() == TrackerStatus.USED) {
                throw new TrackerDeviceException("Tracker device already assigned");
            }

            transactions.setTracker(trackerDevices);

            trackerDevices.setStatus(TrackerStatus.USED);

            trackerDevicesRepository.saveAndFlush(trackerDevices);

            transactionsRepository.saveAndFlush(transactions);

            log.info("Transactions Service: Device assign to transactions successfully");
        } catch (Exception e) {
            getError(e);
            if (e instanceof NotFoundException) {
                throw e;
            }
            throw new TransactionsException("Failed to assign device to transactions");
        }
    }

    @Override
    public Transactions getTransactionByTracker(TrackerDevices trackerDevices) {
        try {
            Transactions transactions = transactionsRepository.findByTracker(trackerDevices);

            if(transactions == null) {
                throw new NotFoundException("Transactions not found");
            }

            log.info("Transactions Service: Get transactions by tracker successfully");

            return transactions;
        } catch (Exception e) {
            getError(e);
            if (e instanceof NotFoundException) {
                throw e;
            }
            throw new NotFoundException("Transactions not found");
        }
    }

    private static void getError(Exception e) {
        log.error("Error Transactions Service:{}", e.getMessage());
    }
}
