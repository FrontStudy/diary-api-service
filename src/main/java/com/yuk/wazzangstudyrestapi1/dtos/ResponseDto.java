package com.yuk.wazzangstudyrestapi1.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseDto {
    private String status;
    private String emsg;
    private Object data;

    private PageInfoDto page;
}