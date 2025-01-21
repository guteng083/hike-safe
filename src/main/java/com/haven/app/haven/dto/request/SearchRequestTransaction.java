package com.haven.app.haven.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SearchRequestTransaction {
    private String search;
    private String status;
    private Integer page;
    private Integer size;
}
