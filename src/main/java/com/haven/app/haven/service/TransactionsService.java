package com.haven.app.haven.service;

import com.haven.app.haven.dto.request.TransactionsRequest;
import com.haven.app.haven.dto.request.TransactionsStatusRequest;
import com.haven.app.haven.dto.response.TransactionsResponse;
import com.haven.app.haven.entity.TrackerDevices;
import com.haven.app.haven.entity.Transactions;

import java.util.List;

public interface TransactionsService {
    TransactionsResponse createTransaction(TransactionsRequest request);
    List<TransactionsResponse> getTransactions();
    TransactionsResponse updateTransactionStatus(String id, TransactionsStatusRequest request);
    Transactions getOne(String id);
    void deviceAssignment(String id, String deviceId);
    Transactions getTransactionByTracker(TrackerDevices trackerDevices);
}
