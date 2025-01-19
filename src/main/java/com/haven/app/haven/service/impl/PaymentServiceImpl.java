package com.haven.app.haven.service.impl;

import com.haven.app.haven.constant.TransactionStatus;
import com.haven.app.haven.dto.request.MidtransCustomerDetails;
import com.haven.app.haven.dto.request.MidtransPaymentLinkRequest;
import com.haven.app.haven.dto.request.MidtransTransactionDetailsRequest;
import com.haven.app.haven.dto.request.MidtransWebhookRequest;
import com.haven.app.haven.entity.Payment;
import com.haven.app.haven.entity.Transactions;
import com.haven.app.haven.entity.Users;
import com.haven.app.haven.exception.NotFoundException;
import com.haven.app.haven.exception.PaymentException;
import com.haven.app.haven.repository.PaymentRepository;
import com.haven.app.haven.repository.TransactionsRepository;
import com.haven.app.haven.service.PaymentService;
import com.haven.app.haven.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final TransactionsRepository transactionsRepository;
    private final UsersService usersService;

    @Value("${midtrans.base-url}")
    protected String MIDTRANS_URL;

    @Value("${midtrans.server-key}")
    protected String MIDTRANS_SECRET_KEY;

    @Override
    public void webhookNotificaction(MidtransWebhookRequest request) {
        try{
            String orderId = extractUUID(request.getOrder_id());
            Transactions transactions = transactionsRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Transaction not found"));

            Payment payment = Payment.builder()
                    .transactions(transactions)
                    .settlementTime(request.getTransaction_time())
                    .paymentMethod(request.getPayment_type())
                    .amount(Double.parseDouble(request.getGross_amount()))
                    .status(request.getTransaction_status())
                    .orderId(request.getOrder_id())
                    .midtransOrderId(request.getTransaction_id())
                    .fraudStatus(request.getFraud_status())
                    .build();

            transactions.setStatus(TransactionStatus.BOOKED);
            transactionsRepository.save(transactions);

            paymentRepository.save(payment);
            log.info("Payment Service: Payment success");
        } catch (Exception e) {
            getError(e);
            throw new PaymentException("Failed to process payment");
        }
        
    }

    @Override
    public void createPaymentLink(String id) {
        try {
            Transactions transactions = transactionsRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Transaction not found"));

            Users user = transactions.getUser();

            MidtransTransactionDetailsRequest transactionDetails = MidtransTransactionDetailsRequest.builder()
                    .order_id(transactions.getId())
                    .gross_amount(transactions.getTotalAmount() + 1000)
                    .build();

            MidtransCustomerDetails customerDetails = MidtransCustomerDetails.builder()
                    .first_name(user.getUsersDetail().getFullName())
                    .email(user.getEmail())
                    .phone(user.getUsersDetail().getPhone())
                    .build();

            MidtransPaymentLinkRequest paymentLinkRequest = MidtransPaymentLinkRequest.builder()
                    .transaction_details(transactionDetails)
                    .customer_details(customerDetails)
                    .build();

            RestClient restClient = RestClient.create();

            String encodedKey = Base64.getEncoder().encodeToString((MIDTRANS_SECRET_KEY + ":").getBytes());

            System.out.println("encodedKey: " + encodedKey);

            ResponseEntity<?> response = restClient.post().uri(MIDTRANS_URL + "/v1/payment-links")
                    .header("Authorization", "Basic " + encodedKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(paymentLinkRequest)
                    .retrieve()
                    .toBodilessEntity();
            if (response.getStatusCode().isError()) {
                throw new PaymentException("Failed to create payment link");
            }

            log.info("Payment Service: Payment link created successfully");
        } catch (Exception e) {
            getError(e);
            throw new PaymentException("Failed to create payment link");
        }

    }

    private static void getError(Exception e) {
        log.error("Error Payment Service:{}", e.getMessage());
    }

    private static String extractUUID(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        int lastHyphenIndex = input.lastIndexOf('-');
        if (lastHyphenIndex > 0) {
            return input.substring(0, lastHyphenIndex);
        }
        return input;
    }
}
