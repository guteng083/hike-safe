package com.haven.app.haven.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MidtransWebhookRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transaction_time;
    private String transaction_status;
    private String transaction_id;
    private String status_message;
    private String status_code;
    private String signature_key;
    private String payment_type;
    private String order_id;
    private String merchant_id;
    private String gross_amount;
    private String fraud_status;
    private String currency;

}
