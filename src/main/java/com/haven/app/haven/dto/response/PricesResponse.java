package com.haven.app.haven.dto.response;

import com.haven.app.haven.entity.Prices;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PricesResponse {
    private String id;
    private String priceType;
    private Double price;

    public static PricesResponse toPricesResponse(Prices prices) {
        return PricesResponse.builder()
                .id(prices.getId())
                .priceType(prices.getPriceType().toString())
                .price(prices.getPrice())
                .build();
    }
}
