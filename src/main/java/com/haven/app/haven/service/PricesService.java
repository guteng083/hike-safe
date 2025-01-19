package com.haven.app.haven.service;

import com.haven.app.haven.dto.request.PricesRequest;
import com.haven.app.haven.dto.response.PricesResponse;

import java.util.List;

public interface PricesService {
    PricesResponse createPrices(PricesRequest prices);
    PricesResponse updatePrices(String id, PricesRequest prices);
    List<PricesResponse> getPrices();
}
