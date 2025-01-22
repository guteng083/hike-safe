package com.haven.app.haven.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class SearchRequest {
    private String search;
    private Integer page;
    private Integer size;
}
