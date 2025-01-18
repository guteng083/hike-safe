package com.haven.app.haven.service;

import com.haven.app.haven.dto.request.TransactionsRequest;
import com.haven.app.haven.dto.request.TransactionsStatusRequest;
import com.haven.app.haven.dto.response.CommonResponse;
import com.haven.app.haven.dto.response.CommonResponseWithData;
import com.haven.app.haven.dto.response.TransactionsResponse;
import com.haven.app.haven.entity.Transactions;

import java.util.List;

public interface TransactionsService {
    CommonResponseWithData<TransactionsResponse> createTransaction(TransactionsRequest request);
    CommonResponseWithData<List<TransactionsResponse>> getTransactions();
    CommonResponseWithData<TransactionsResponse> updateTransactionStatus(String id, TransactionsStatusRequest request);
    Transactions getOne(String id);
    CommonResponse deviceAssignment(String id, String deviceId);
}
