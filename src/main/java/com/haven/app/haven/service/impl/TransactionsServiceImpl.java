package com.haven.app.haven.service.impl;

import com.haven.app.haven.constant.TransactionStatus;
import com.haven.app.haven.dto.request.TransactionsRequest;
import com.haven.app.haven.dto.request.TransactionsStatusRequest;
import com.haven.app.haven.dto.response.TransactionsResponse;
import com.haven.app.haven.entity.Ticket;
import com.haven.app.haven.entity.Transactions;
import com.haven.app.haven.entity.Users;
import com.haven.app.haven.repository.TransactionsRepository;
import com.haven.app.haven.repository.UsersRepository;
import com.haven.app.haven.service.TransactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionsServiceImpl implements TransactionsService {
    private final TransactionsRepository transactionsRepository;
    private final UsersRepository usersRepository;

    @Override
    public TransactionsResponse createTransaction(TransactionsRequest request) {
        Users user = usersRepository.findById(request.getUserId()).orElse(null);

        Transactions transactions = Transactions.builder()
                .user(user)
                .startDate(LocalDateTime.parse(request.getStartDate()))
                .endDate(LocalDateTime.parse(request.getEndDate()))
                .build();

        transactionsRepository.save(transactions);

        List<Ticket> tickets = request.getTickets().stream().map(ticketRequest ->
                Ticket.builder()
                        .transactionId(transactions)
                        .hikerName(ticketRequest.getHikerName())
                        .address(ticketRequest.getAddress())
                        .phoneNumber(ticketRequest.getPhoneNumber())
                        .build()
                ).toList();

        transactions.setTickets(tickets);

        transactionsRepository.saveAndFlush(transactions);

        return TransactionsResponse.toTransactionResponse(transactions);
    }

    @Override
    public List<TransactionsResponse> getTransactions() {
        List<Transactions> transactions = transactionsRepository.findAll();

        return transactions.stream()
                .map(TransactionsResponse::toTransactionResponse).toList();
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
        return transactionsRepository.findById(UUID.fromString(id)).orElse(null);
    }
}
