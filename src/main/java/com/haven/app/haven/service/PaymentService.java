package com.haven.app.haven.service;

import com.haven.app.haven.dto.request.MidtransWebhookRequest;

public interface PaymentService {
    void webhookNotification(MidtransWebhookRequest $request);
    String createPaymentLink(String $id);
}
