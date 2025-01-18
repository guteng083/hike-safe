package com.haven.app.haven.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ValidationErrorResponse {
    private String message;
    private List<Map<String, List<String>>> errors;
}
