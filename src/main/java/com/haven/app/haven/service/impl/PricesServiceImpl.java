package com.haven.app.haven.service.impl;

import com.haven.app.haven.constant.PriceType;
import com.haven.app.haven.dto.request.PricesRequest;
import com.haven.app.haven.dto.request.SearchRequestTransaction;
import com.haven.app.haven.dto.response.PricesResponse;
import com.haven.app.haven.entity.Prices;
import com.haven.app.haven.exception.CoordinateException;
import com.haven.app.haven.exception.NotFoundException;
import com.haven.app.haven.exception.PriceException;
import com.haven.app.haven.repository.PricesRepository;
import com.haven.app.haven.service.PricesService;
import com.haven.app.haven.specification.PriceSpecification;
import com.haven.app.haven.utils.LogUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
        try {
            Prices price = Prices.builder()
                    .priceType(PriceType.getPriceType(prices.getPriceType()))
                    .price(prices.getPrice())
                    .build();

            pricesRepository.save(price);

            LogUtils.logSuccess("PricesService", "createPrices");

            return PricesResponse.toPricesResponse(price);
        } catch (Exception e) {
            LogUtils.getError("PricesService.createPrices", e);
            throw new PriceException("Failed to create prices");
        }
    }

    @Override
    public PricesResponse updatePrices(String id, PricesRequest prices) {
        try {
            Prices price = pricesRepository.findById(id).orElseThrow(() -> new NotFoundException("Price Not Found"));
            price.setPriceType(PriceType.getPriceType(prices.getPriceType()));
            price.setPrice(prices.getPrice());

            pricesRepository.save(price);

            LogUtils.logSuccess("PricesService", "updatePrices");

            return PricesResponse.toPricesResponse(price);
        } catch (Exception e) {
            LogUtils.getError("PricesService.updatePrices", e);
            if (e instanceof NotFoundException){
                throw e;
            }
            throw new PriceException("Failed to update prices");
        }
    }

    @Override
    public Page<PricesResponse> getPrices(SearchRequestTransaction searchRequest) {
        try {

            Pageable pageable = PageRequest.of(searchRequest.getPage()-1, searchRequest.getSize());

            Specification<Prices> specification  = PriceSpecification.getSpecification(searchRequest);
            Page<Prices> prices = pricesRepository.findAll(specification,pageable);

            LogUtils.logSuccess("PricesService", "getPrices");

            return prices.map(PricesResponse::toPricesResponse);
        } catch (Exception e) {
            LogUtils.getError("PricesService.getPrices", e);
            if(e instanceof NotFoundException){
                throw new NotFoundException("Prices list not found");
            }
            throw new PriceException("Failed to get prices list");
        }
    }

    @Override
    public void deletePrice(String id) {
        try {
            pricesRepository.deleteById(id);
            LogUtils.logSuccess("PricesService", "deletePrice");
        } catch (Exception e){
            LogUtils.getError("PricesService.deletePrice", e);
            throw new PriceException("Failed to delete price");
        }
    }
}
