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
import com.haven.app.haven.utils.LogUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final TransactionsRepository transactionsRepository;

    @Value("${midtrans.base-url}")
    protected String MIDTRANS_URL;

    @Value("${midtrans.server-key}")
    protected String MIDTRANS_SECRET_KEY;

    @Override
    public void webhookNotification(MidtransWebhookRequest request) {
        try{
            String orderId = extractUUID(request.getOrder_id());
            Transactions transactions = transactionsRepository.findById(orderId)
                    .orElseThrow(() -> new NotFoundException("Transaction not found"));

            Payment payment = paymentRepository.findByOrderId(request.getOrder_id());
            if (payment != null) {
                payment.setStatus(request.getTransaction_status());
                paymentRepository.save(payment);
            } else {
                Payment newPayment = Payment.builder()
                        .transactions(transactions)
                        .settlementTime(request.getTransaction_time())
                        .paymentMethod(request.getPayment_type())
                        .amount(Double.parseDouble(request.getGross_amount()))
                        .status(request.getTransaction_status())
                        .orderId(request.getOrder_id())
                        .midtransOrderId(request.getTransaction_id())
                        .fraudStatus(request.getFraud_status())
                        .build();

                transactions.setPayment(newPayment);
            }

            if (request.getTransaction_status().equalsIgnoreCase("settlement"))
            {
                transactions.setStatus(TransactionStatus.BOOKED);
            }
            transactionsRepository.save(transactions);

            LogUtils.logSuccess("PaymentService", "webHookNotification");
        } catch (Exception e) {
            LogUtils.getError("PaymentService.webHookNotification", e);
            if(e instanceof NotFoundException) {
                throw e;
            }
            throw new PaymentException("Failed to process payment");
        }
        
    }

    @Override
    public String createPaymentLink(String id) {
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

            ResponseEntity<Map<String, String>> response = restClient.post().uri(MIDTRANS_URL + "/v1/payment-links")
                    .header("Authorization", "Basic " + encodedKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(paymentLinkRequest)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Map<String, String>>() {});

            if (response.getStatusCode().isError() || response.getBody() == null) {
                throw new PaymentException("Failed to create payment link");
            }

            String paymentUrl = response.getBody().get("payment_url");

            transactions.setPaymentUrl(paymentUrl);

            transactions.setStatus(TransactionStatus.PENDING);

            transactionsRepository.saveAndFlush(transactions);

            LogUtils.logSuccess("PaymentService", "createPaymentLink");

            return paymentUrl;
        } catch (Exception e) {
            LogUtils.getError("PaymentService.createPaymentLink", e);
            if (e instanceof NotFoundException) {
                throw e;
            }
            throw new PaymentException("Failed to create payment link");
        }

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
