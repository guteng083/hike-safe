package com.haven.app.haven.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse <T>{
    private String message;
    private T data;
    private PagingResponse paging;
}
