package com.haven.app.haven.service;

import com.haven.app.haven.dto.request.SearchRequest;
import com.haven.app.haven.dto.request.SearchRequestTransaction;
import com.haven.app.haven.dto.request.TransactionsRequest;
import com.haven.app.haven.dto.request.TransactionsStatusRequest;
import com.haven.app.haven.dto.response.TransactionsResponse;
import com.haven.app.haven.dto.response.TransactionsResponseWithCoordinate;
import com.haven.app.haven.entity.TrackerDevices;
import com.haven.app.haven.entity.Transactions;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionsService {
    TransactionsResponse createTransaction(TransactionsRequest request);
    Page<TransactionsResponse> getTransactions(SearchRequestTransaction searchRequest);
    TransactionsResponse getTransactionById(String id);
    Page<TransactionsResponse> getTransactionByUser(Integer page, Integer size);
    List<TransactionsResponse> getTransactionByUserWithoutPage();
    TransactionsResponse updateTransactionStatus(String id, TransactionsStatusRequest request);
    Transactions getOne(String id);
    void deviceAssignment(String id, String deviceId);
    Transactions getTransactionByTracker(TrackerDevices trackerDevices);
}
