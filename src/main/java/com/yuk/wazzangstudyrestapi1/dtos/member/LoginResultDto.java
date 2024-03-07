package com.yuk.wazzangstudyrestapi1.dtos.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LoginResultDto {
    private Long id;
    private String email;
    private String jtoken;
    private String userrole;
}