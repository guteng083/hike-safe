package com.haven.app.haven.controller;

import com.haven.app.haven.constant.Endpoint;
import com.haven.app.haven.dto.request.MidtransWebhookRequest;
import com.haven.app.haven.dto.response.CommonResponse;
import com.haven.app.haven.service.PaymentService;
import com.haven.app.haven.utils.ResponseUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoint.API_PAYMENT)
@Tag(name="Payments", description = "APIs for payments midtrans")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping(path="/notification")
    public CommonResponse webhookNotificaction(@RequestBody MidtransWebhookRequest request)
    {
        paymentService.webhookNotificaction(request);

        return ResponseUtils.response("Payment notification success");
    }

    @PostMapping(path="/{id}/create-payment-link")
    public CommonResponse createPaymentLink(@PathVariable String id)
    {
        paymentService.createPaymentLink(id);

        return ResponseUtils.response("Payment link created");
    }
}
