package com.haven.app.haven.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MidtransTransactionDetailsRequest {
    private String order_id;
    private double gross_amount;
}
