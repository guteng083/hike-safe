package com.haven.app.haven.service.impl;

import com.haven.app.haven.constant.PriceType;
import com.haven.app.haven.dto.request.PricesRequest;
import com.haven.app.haven.dto.response.PricesResponse;
import com.haven.app.haven.entity.Prices;
import com.haven.app.haven.repository.PricesRepository;
import com.haven.app.haven.service.PricesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PricesServiceImpl implements PricesService {
    private final PricesRepository pricesRepository;

    @Override
    public PricesResponse createPrices(PricesRequest prices) {
        Prices price = Prices.builder()
                .priceType(PriceType.getPriceType(prices.getPriceType()))
                .price(prices.getPrice())
                .build();

        pricesRepository.save(price);

        return PricesResponse.toPricesResponse(price);
    }

    @Override
    public PricesResponse updatePrices(String id, PricesRequest prices) {
        Prices price = pricesRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        price.setPriceType(PriceType.getPriceType(prices.getPriceType()));
        price.setPrice(prices.getPrice());

        pricesRepository.save(price);

        return PricesResponse.toPricesResponse(price);
    }

    @Override
    public List<PricesResponse> getPrices() {
        List<Prices> prices = pricesRepository.findAll();

        return prices.stream().map(PricesResponse::toPricesResponse).toList();
    }
}
