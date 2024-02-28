package com.yuk.wazzangstudyrestapi1.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RequestMemberListDto {
    private int offset;
    private int size;

    private Long memberId;
    private String email;
    private String userrole;
    private String nickname;
    private String birthDate;
    private String name;
    private String gender;
    private Boolean active;
}
