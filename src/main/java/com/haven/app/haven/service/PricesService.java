package com.haven.app.haven.service;

import com.haven.app.haven.dto.request.PricesRequest;
import com.haven.app.haven.dto.request.SearchRequestTransaction;
import com.haven.app.haven.dto.response.PricesResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PricesService {
    PricesResponse createPrices(PricesRequest prices);
    PricesResponse updatePrices(String id, PricesRequest prices);
    Page<PricesResponse> getPrices(SearchRequestTransaction searchRequest);
}
