package com.haven.app.haven.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagingResponse {
    private Long totalElements;
    private Integer totalPages;
    private Integer pageSize;
    private Integer pageNumber;
}
