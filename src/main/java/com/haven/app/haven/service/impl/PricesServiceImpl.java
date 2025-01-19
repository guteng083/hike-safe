package com.haven.app.haven.service.impl;

import com.haven.app.haven.constant.PriceType;
import com.haven.app.haven.dto.request.PricesRequest;
import com.haven.app.haven.dto.response.PricesResponse;
import com.haven.app.haven.entity.Prices;
import com.haven.app.haven.exception.CoordinateException;
import com.haven.app.haven.exception.NotFoundException;
import com.haven.app.haven.repository.PricesRepository;
import com.haven.app.haven.service.PricesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PricesServiceImpl implements PricesService {
    private final PricesRepository pricesRepository;

    @Override
    public PricesResponse createPrices(PricesRequest prices) {
        try {
            Prices price = Prices.builder()
                    .priceType(PriceType.getPriceType(prices.getPriceType()))
                    .price(prices.getPrice())
                    .build();

            pricesRepository.save(price);

            log.info("Price Service: Price created successfully");

            return PricesResponse.toPricesResponse(price);
        } catch (Exception e) {
            getError(e);
            throw new CoordinateException("Failed to create prices");
        }
    }

    @Override
    public PricesResponse updatePrices(String id, PricesRequest prices) {
        try {
            Prices price = pricesRepository.findById(id).orElseThrow(() -> new NotFoundException("Price Not Found"));
            price.setPriceType(PriceType.getPriceType(prices.getPriceType()));
            price.setPrice(prices.getPrice());

            pricesRepository.save(price);

            log.info("Price Service: Price updated successfully");

            return PricesResponse.toPricesResponse(price);
        } catch (Exception e) {
            getError(e);
            if (e instanceof NotFoundException){
                throw e;
            }
            throw new CoordinateException("Failed to update prices");
        }
    }

    @Override
    public List<PricesResponse> getPrices() {
        try {
            List<Prices> prices = pricesRepository.findAll();

            if(prices.isEmpty()) {
                throw new NotFoundException("Prices list not found");
            }

            log.info("Prices Service: Get prices list successfully");

            return prices.stream().map(PricesResponse::toPricesResponse).toList();
        } catch (Exception e) {
            getError(e);
            if(e instanceof NotFoundException){
                throw new NotFoundException("Prices list not found");
            }
            throw new CoordinateException("Failed to get prices list");
        }
    }

    private static void getError(Exception e) {
        log.error("Error Price Service:{}", e.getMessage());
    }
}
