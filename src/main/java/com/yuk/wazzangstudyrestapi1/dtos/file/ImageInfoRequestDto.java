package com.yuk.wazzangstudyrestapi1.dtos.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ImageInfoRequestDto {
    private String imgs;
    private String dummy;
}
