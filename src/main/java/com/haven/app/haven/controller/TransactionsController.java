package com.haven.app.haven.controller;

import com.haven.app.haven.constant.Constant;
import com.haven.app.haven.dto.request.SearchRequest;
import com.haven.app.haven.dto.request.SearchRequestTransaction;
import com.haven.app.haven.dto.request.TransactionsRequest;
import com.haven.app.haven.dto.request.TransactionsStatusRequest;
import com.haven.app.haven.dto.response.*;
import com.haven.app.haven.service.TransactionsService;
import com.haven.app.haven.utils.ResponseUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.TRANSACTIONS_API)
@RequiredArgsConstructor
@Validated
@Tag(name="Transaction", description = "APIs for transaction")
public class TransactionsController {
    private final TransactionsService transactionsService;

    @PostMapping
    public CommonResponseWithData<TransactionsResponse> createTransaction(@RequestBody TransactionsRequest transactionsRequest) {
        TransactionsResponse transactionsResponse = transactionsService.createTransaction(transactionsRequest);

        return ResponseUtils.responseWithData("Transaction Created", transactionsResponse);
    }

    @GetMapping
    public PageResponse<List<TransactionsResponse>> getAllTransactions(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "status", required = false) String status,
            @Valid
            @NotNull(message = "Page number is required")
            @Min(value = 1, message = "Page number cannot be zero negative")
            @RequestParam(defaultValue = "1") Integer page,

            @Valid
            @NotNull(message = "Page size is required")
            @Min(value = 1, message = "Page size cannot be zero or negative")
            @RequestParam(defaultValue = "10") Integer size
    ) {
        SearchRequestTransaction searchRequest = SearchRequestTransaction.builder()
                .search(search)
                .status(status)
                .page(page)
                .size(size)
                .build();

        Page<TransactionsResponse> transactionsResponses = transactionsService.getTransactions(searchRequest);
        return ResponseUtils.responseWithPage("Get All Transactions", transactionsResponses);
    }

    @GetMapping("/{id}")
    public CommonResponseWithData<TransactionsResponseWithCoordinate> getTransactionById(@PathVariable String id) {
        TransactionsResponseWithCoordinate transactionsResponse = transactionsService.getTransactionById(id);
        return ResponseUtils.responseWithData("Get Transaction By Id", transactionsResponse);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getTransactionsByUserId(
            @RequestParam(defaultValue = "true") boolean pagination,

            @Valid
            @Min(value = 1, message = "Page number cannot be zero negative")
            @RequestParam(defaultValue = "1") Integer page,

            @Valid
            @Min(value = 1, message = "Size number cannot be zero negative")
            @RequestParam(defaultValue = "10") Integer size
    ) {
        if (pagination) {
            Page<TransactionsResponse> transactionsResponses = transactionsService.getTransactionByUser(page,size);
            return ResponseEntity.ok(ResponseUtils.responseWithPage("Get Transaction By User", transactionsResponses));
        } else {
            List<TransactionsResponse> transactionsResponses = transactionsService.getTransactionByUserWithoutPage();
            return ResponseEntity.ok(ResponseUtils.responseWithData("Get Transaction By User", transactionsResponses));
        }
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
