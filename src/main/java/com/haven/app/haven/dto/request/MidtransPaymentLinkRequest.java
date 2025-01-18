package com.haven.app.haven.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MidtransPaymentLinkRequest {

    private MidtransTransactionDetailsRequest transaction_details;
    private MidtransCustomerDetails customer_details;

}
