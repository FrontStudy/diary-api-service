package com.yuk.wazzangstudyrestapi1.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PageInfoDto {
    private Long totalElements;
    private Long totalPages;
}