package com.haven.app.haven.service;

import com.haven.app.haven.dto.request.TransactionsRequest;
import com.haven.app.haven.dto.response.TransactionsResponse;

public interface TransactionsService {
    TransactionsResponse createTransaction(TransactionsRequest request);
}
