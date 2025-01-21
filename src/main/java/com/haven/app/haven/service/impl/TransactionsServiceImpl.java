package com.haven.app.haven.service.impl;

import com.haven.app.haven.constant.PriceType;
import com.haven.app.haven.constant.TrackerStatus;
import com.haven.app.haven.constant.TransactionStatus;
import com.haven.app.haven.dto.request.SearchRequest;
import com.haven.app.haven.dto.request.TransactionsRequest;
import com.haven.app.haven.dto.request.TransactionsStatusRequest;
import com.haven.app.haven.dto.response.CommonResponse;
import com.haven.app.haven.dto.response.CommonResponseWithData;
import com.haven.app.haven.dto.response.TransactionsResponse;
import com.haven.app.haven.dto.response.TransactionsResponseWithCoordinate;
import com.haven.app.haven.entity.*;
import com.haven.app.haven.exception.NotFoundException;
import com.haven.app.haven.exception.TrackerDeviceException;
import com.haven.app.haven.exception.TransactionsException;
import com.haven.app.haven.repository.*;
import com.haven.app.haven.service.TrackerDevicesService;
import com.haven.app.haven.service.TransactionsService;
import com.haven.app.haven.service.UsersService;
import com.haven.app.haven.specification.TransactionSpecification;
import com.haven.app.haven.utils.LogUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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

            LogUtils.logSuccess("TransactionsService", "createTransaction");

            return TransactionsResponse.toTransactionResponse(transactions);
        } catch (Exception e) {
            LogUtils.getError("TransactionsService.createTransaction", e);
            if (e instanceof NotFoundException) {
                throw e;
            }
            throw new TransactionsException("Failed to create transaction");
        }
    }

    @Override
    public Page<TransactionsResponse> getTransactions(SearchRequest searchRequest) {
        try {
            Pageable pageable = PageRequest.of(searchRequest.getPage() - 1, searchRequest.getSize());

            Specification<Transactions> specification =
                    TransactionSpecification.getSpecification(searchRequest);

            Page<Transactions> transactions = transactionsRepository.findAll(specification,pageable);

            if(transactions.isEmpty()) {
                throw new NotFoundException("Transactions not found");
            }

            LogUtils.logSuccess("TransactionsService", "getTransactions");

            return transactions.map(TransactionsResponse::toTransactionResponse);
        } catch (Exception e) {
            LogUtils.getError("TransactionsService.getTransactions", e);
            if (e instanceof NotFoundException) {
                throw e;
            }
            throw new TransactionsException("Failed to get transactions list");
        }
    }

    @Override
    public TransactionsResponseWithCoordinate getTransactionById(String id) {
        try {
            Transactions transactions = getOne(id);

            LogUtils.logSuccess("TransactionsService", "getTransactionById");

            return TransactionsResponseWithCoordinate.toTransactionResponseWithCoordinates(transactions);
        } catch (Exception e) {
            LogUtils.getError("TransactionsService.getTransactionById", e);
            if (e instanceof NotFoundException) {
                throw e;
            }
            throw new TransactionsException("Failed to get transaction by id");
        }
    }

    @Override
    public Page<TransactionsResponse> getTransactionByUser(Integer page, Integer size) {
        try {
            Users user = usersService.getMe();
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Transactions> transactions = transactionsRepository.findAllByUser(user, pageable);
            LogUtils.logSuccess("TransactionsService", "getTransactionByUser");

            return transactions.map(TransactionsResponse::toTransactionResponse);
        } catch (Exception e) {
            LogUtils.getError("TransactionsService.getTransactionByUser", e);
            if (e instanceof NotFoundException) {
                throw e;
            }
            throw new TransactionsException("Failed to get transactions by user");
        }
    }

    @Override
    public List<TransactionsResponse> getTransactionByUserWithoutPage() {
        try {
            Users user = usersService.getMe();
            List<Transactions> transactions = transactionsRepository.findAllByUser(user);
            LogUtils.logSuccess("TransactionsService", "getTransactionByUserWithoutPage");

            return transactions.stream().map(TransactionsResponse::toTransactionResponse).collect(Collectors.toList());
        } catch (Exception e) {
            LogUtils.getError("TransactionsService.getTransactionByUserWithoutPage", e);
            if (e instanceof NotFoundException) {
                throw e;
            }
            throw new TransactionsException("Failed to get transactions by user");
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

            LogUtils.logSuccess("TransactionsService", "updateTransactionStatus");

            return TransactionsResponse.toTransactionResponse(transactions);
        } catch (Exception e) {
            LogUtils.getError("TransactionsService.updateTransactionStatus", e);
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
            LogUtils.getError("TransactionsService.getOne", e);
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

            transactions.setStatus(TransactionStatus.START);

            trackerDevicesRepository.saveAndFlush(trackerDevices);

            transactionsRepository.saveAndFlush(transactions);

            LogUtils.logSuccess("TransactionsService", "deviceAssignment");
        } catch (Exception e) {
            LogUtils.getError("TransactionsService.deviceAssignment", e);
            if (e instanceof NotFoundException) {
                throw e;
            }
            throw new TransactionsException("Failed to assign device to transactions");
        }
    }

    @Override
    public Transactions getTransactionByTracker(TrackerDevices trackerDevices) {
        try {
            List<Transactions> transactionsList = transactionsRepository.findAllByTracker(trackerDevices);

            if(transactionsList == null) {
                throw new NotFoundException("Transactions not found");
            }

            LogUtils.logSuccess("TransactionsService", "getTransactionByTracker");

            return transactionsList.stream()
                    .filter(transactions -> transactions.getStatus() == TransactionStatus.START)
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Transactions not found"));
        } catch (Exception e) {
            LogUtils.getError("TransactionsService.getTransactionByTracker", e);
            if (e instanceof NotFoundException) {
                throw e;
            }
            throw new NotFoundException("Transactions not found");
        }
    }
}
