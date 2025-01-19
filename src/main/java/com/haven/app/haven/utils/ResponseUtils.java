package com.haven.app.haven.utils;

import com.haven.app.haven.dto.response.CommonResponse;
import com.haven.app.haven.dto.response.CommonResponseWithData;
import com.haven.app.haven.dto.response.PageResponse;
import com.haven.app.haven.dto.response.PagingResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public class ResponseUtils {
    public static <T> CommonResponseWithData<T> responseWithData(String message, T data) {
        return CommonResponseWithData.<T>builder()
                .message(message)
                .data(data)
                .build();

    }

    public static CommonResponse response(String message) {
        return CommonResponse.builder()
                .message(message)
                .build();

    }
    public static <T> PageResponse<List<T>> responseWithPage(String message, Page<T> data) {

        return PageResponse.<List<T>>builder()
                .message(message)
                .data(data.getContent())
                .paging(PagingResponse.builder()
                        .pageNumber(data.getNumber()+1)
                        .pageSize(data.getSize())
                        .totalPages(data.getTotalPages())
                        .totalElements(data.getTotalElements())
                        .build())
                .build();
    }
}
