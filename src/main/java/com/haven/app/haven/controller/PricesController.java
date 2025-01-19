package com.haven.app.haven.controller;

import com.haven.app.haven.constant.Constant;
import com.haven.app.haven.dto.request.PricesRequest;
import com.haven.app.haven.dto.response.CommonResponseWithData;
import com.haven.app.haven.dto.response.PricesResponse;
import com.haven.app.haven.entity.Prices;
import com.haven.app.haven.service.PricesService;
import com.haven.app.haven.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.PRICE_API)
@RequiredArgsConstructor
public class PricesController {
    private final PricesService pricesService;

    @PostMapping
    public CommonResponseWithData<PricesResponse> createPrices(@RequestBody PricesRequest request) {
        PricesResponse pricesResponse = pricesService.createPrices(request);

        return ResponseUtils.ResponseWithData("Price Created", pricesResponse);
    }

    @GetMapping
    public CommonResponseWithData<List<PricesResponse>> getAllPrices() {
        List<PricesResponse> pricesResponses = pricesService.getPrices();

        return ResponseUtils.ResponseWithData("Prices List", pricesResponses);
    }

    @PutMapping("/{id}")
    public CommonResponseWithData<PricesResponse> updatePrice(@PathVariable String id, @RequestBody PricesRequest request) {
        PricesResponse pricesResponse = pricesService.updatePrices(id, request);

        return ResponseUtils.ResponseWithData("Price Updated", pricesResponse);
    }
}
