package com.haven.app.haven.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SearchRequest {
    private String name;
    private String email;
    private Integer page;
    private Integer size;
}
