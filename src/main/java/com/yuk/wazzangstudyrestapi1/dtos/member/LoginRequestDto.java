package com.yuk.wazzangstudyrestapi1.dtos.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDto {
    private String email;
    private String passwd;
    private String jtoken;
}
