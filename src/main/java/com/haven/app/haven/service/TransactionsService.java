package com.haven.app.haven.service;

import com.haven.app.haven.dto.request.TransactionsRequest;
import com.haven.app.haven.dto.request.TransactionsStatusRequest;
import com.haven.app.haven.dto.response.TransactionsResponse;
import com.haven.app.haven.entity.TrackerDevices;
import com.haven.app.haven.entity.Transactions;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionsService {
    TransactionsResponse createTransaction(TransactionsRequest request);
    Page<TransactionsResponse> getTransactions(Integer page, Integer size);
    TransactionsResponse getTransactionById(String id);
    List<TransactionsResponse> getTransactionByUser();
    TransactionsResponse updateTransactionStatus(String id, TransactionsStatusRequest request);
    Transactions getOne(String id);
    void deviceAssignment(String id, String deviceId);
    Transactions getTransactionByTracker(TrackerDevices trackerDevices);
}
