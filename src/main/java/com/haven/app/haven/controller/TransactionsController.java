package com.haven.app.haven.controller;

import com.haven.app.haven.constant.Constant;
import com.haven.app.haven.dto.request.TransactionsRequest;
import com.haven.app.haven.dto.request.TransactionsStatusRequest;
import com.haven.app.haven.dto.response.CommonResponseWithData;
import com.haven.app.haven.dto.response.TransactionsResponse;
import com.haven.app.haven.service.TransactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.TRANSACTIONS_API)
@RequiredArgsConstructor
public class TransactionsController {
    private final TransactionsService transactionsService;

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionsRequest transactionsRequest) {
        CommonResponseWithData<TransactionsResponse> transactionsResponse = transactionsService.createTransaction(transactionsRequest);

        return ResponseEntity.ok(transactionsResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllTransactions() {
        CommonResponseWithData<List<TransactionsResponse>> transactionsResponses = transactionsService.getTransactions();

        return ResponseEntity.ok(transactionsResponses);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateTransactionStatus(@PathVariable("id") String id, @RequestBody TransactionsStatusRequest request) {
        CommonResponseWithData<TransactionsResponse> transactionsResponse = transactionsService.updateTransactionStatus(id, request);

        return ResponseEntity.ok(transactionsResponse);
    }
}
