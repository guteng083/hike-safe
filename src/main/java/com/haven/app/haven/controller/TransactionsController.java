package com.haven.app.haven.controller;

import com.haven.app.haven.constant.Constant;
import com.haven.app.haven.dto.request.TransactionsRequest;
import com.haven.app.haven.dto.request.TransactionsStatusRequest;
import com.haven.app.haven.dto.response.CommonResponse;
import com.haven.app.haven.dto.response.CommonResponseWithData;
import com.haven.app.haven.dto.response.TransactionsResponse;
import com.haven.app.haven.service.TransactionsService;
import com.haven.app.haven.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.TRANSACTIONS_API)
@RequiredArgsConstructor
public class TransactionsController {
    private final TransactionsService transactionsService;

    @PostMapping
    public CommonResponseWithData<TransactionsResponse> createTransaction(@RequestBody TransactionsRequest transactionsRequest) {
        TransactionsResponse transactionsResponse = transactionsService.createTransaction(transactionsRequest);

        return ResponseUtils.responseWithData("Transaction Created", transactionsResponse);
    }

    @GetMapping
    public CommonResponseWithData<List<TransactionsResponse>> getAllTransactions() {
        List<TransactionsResponse> transactionsResponses = transactionsService.getTransactions();

        return ResponseUtils.responseWithData("Get All Transactions", transactionsResponses);
    }

    @PatchMapping("/{id}/status")
    public CommonResponseWithData<TransactionsResponse> updateTransactionStatus(@PathVariable("id") String id, @RequestBody TransactionsStatusRequest request) {
        TransactionsResponse transactionsResponse = transactionsService.updateTransactionStatus(id, request);

        return ResponseUtils.responseWithData("Transaction Updated", transactionsResponse);
    }

    @PatchMapping("/{id}/device/{deviceId}")
    public CommonResponse deviceAssignment(@PathVariable("id") String id, @PathVariable("deviceId") String deviceId) {
        transactionsService.deviceAssignment(id, deviceId);

        return ResponseUtils.response("Device has been assigned");
    }
}
