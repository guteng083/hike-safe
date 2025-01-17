package com.haven.app.haven.utils;

import com.haven.app.haven.dto.response.CommonResponse;
import com.haven.app.haven.dto.response.CommonResponseWithData;

public class ResponseUtils {
    public static <T> CommonResponseWithData<T> ResponseWithData(String message, T data) {
        return CommonResponseWithData.<T>builder()
                .message(message)
                .data(data)
                .build();

    }

    public static CommonResponse Response(String message) {
        return CommonResponse.builder()
                .message(message)
                .build();

    }
}
