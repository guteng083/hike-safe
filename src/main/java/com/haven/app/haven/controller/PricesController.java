package com.haven.app.haven.controller;

import com.haven.app.haven.constant.Constant;
import com.haven.app.haven.dto.request.PricesRequest;
import com.haven.app.haven.dto.request.SearchRequestTransaction;
import com.haven.app.haven.dto.response.CommonResponseWithData;
import com.haven.app.haven.dto.response.PageResponse;
import com.haven.app.haven.dto.response.PricesResponse;
import com.haven.app.haven.service.PricesService;
import com.haven.app.haven.utils.ResponseUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.PRICE_API)
@RequiredArgsConstructor
@Validated
@Tag(name = "Ticket Price Management", description = "APIs for ticket price management")
public class PricesController {
    private final PricesService pricesService;

    @PostMapping
    public CommonResponseWithData<PricesResponse> createPrices(@RequestBody PricesRequest request) {
        PricesResponse pricesResponse = pricesService.createPrices(request);

        return ResponseUtils.responseWithData("Price Created", pricesResponse);
    }

    @GetMapping
    public PageResponse<List<PricesResponse>> getAllPrice(
            @Valid
            @Min(value = 1, message = "Page must be at least 1")
            @RequestParam(required = false, defaultValue = "1") Integer page,

            @Valid
            @Min(value = 1, message = "Size must at least 1")
            @RequestParam(required = false, defaultValue = "10") Integer size,

            @Valid
            @RequestParam(required = false) String search,

            @Valid
            @RequestParam(required = false) String status) {
        SearchRequestTransaction searchRequest = SearchRequestTransaction.builder()
                .page(page)
                .size(size)
                .search(search)
                .status(status)
                .build();

        Page<PricesResponse> pricesResponses = pricesService.getPrices(searchRequest);
        return ResponseUtils.responseWithPage("Prices List", pricesResponses);
    }

    @PutMapping("/{id}")
    public CommonResponseWithData<PricesResponse> updatePrice(@PathVariable String id, @RequestBody PricesRequest request) {
        PricesResponse pricesResponse = pricesService.updatePrices(id, request);

        return ResponseUtils.responseWithData("Price Updated", pricesResponse);
    }
}
