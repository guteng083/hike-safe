package com.haven.app.haven.service;

import com.haven.app.haven.dto.request.MidtransWebhookRequest;

public interface PaymentService {
    void webhookNotificaction(MidtransWebhookRequest $request);
    String createPaymentLink(String $id);
}
