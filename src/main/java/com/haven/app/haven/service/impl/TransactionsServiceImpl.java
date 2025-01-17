package com.haven.app.haven.service.impl;

import com.haven.app.haven.dto.request.TransactionsRequest;
import com.haven.app.haven.dto.response.TransactionsResponse;
import com.haven.app.haven.entity.Transactions;
import com.haven.app.haven.repository.TicketRepository;
import com.haven.app.haven.repository.TransactionsRepository;
import com.haven.app.haven.service.TransactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionsServiceImpl implements TransactionsService {
    private final TransactionsRepository transactionsRepository;
    private final TicketRepository ticketRepository;

    @Override
    public TransactionsResponse createTransaction(TransactionsRequest request) {
        Transactions transactions = Transactions.builder()
                .user()
                .build();
    }
}
