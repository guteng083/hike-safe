package com.haven.app.haven.controller;

import com.haven.app.haven.constant.Constant;
import com.haven.app.haven.dto.request.TransactionsRequest;
import com.haven.app.haven.dto.request.TransactionsStatusRequest;
import com.haven.app.haven.dto.response.CommonResponse;
import com.haven.app.haven.dto.response.CommonResponseWithData;
import com.haven.app.haven.dto.response.PageResponse;
import com.haven.app.haven.dto.response.TransactionsResponse;
import com.haven.app.haven.service.TransactionsService;
import com.haven.app.haven.utils.ResponseUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.TRANSACTIONS_API)
@RequiredArgsConstructor
@Validated
public class TransactionsController {
    private final TransactionsService transactionsService;

    @PostMapping
    public CommonResponseWithData<TransactionsResponse> createTransaction(@RequestBody TransactionsRequest transactionsRequest) {
        TransactionsResponse transactionsResponse = transactionsService.createTransaction(transactionsRequest);

        return ResponseUtils.responseWithData("Transaction Created", transactionsResponse);
    }

    @GetMapping
    public PageResponse<List<TransactionsResponse>> getAllTransactions(
            @Valid
            @NotNull(message = "Page number is required")
            @Min(value = 1, message = "Page number cannot be zero negative")
            @RequestParam(defaultValue = "1") Integer page,

            @Valid
            @NotNull(message = "Page size is required")
            @Min(value = 1, message = "Page size cannot be zero or negative")
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<TransactionsResponse> transactionsResponses = transactionsService.getTransactions(page,size);

        return ResponseUtils.responseWithPage("Get All Transactions", transactionsResponses);
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
