package com.yuk.wazzangstudyrestapi1.dtos.member;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class LoginRequestDto {
    private String email;
    private String passwd;
    private String jtoken;
}
