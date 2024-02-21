package com.yuk.wazzangstudyrestapi1.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequestDto {

    private String email;
    private String passwd;
    private String jtoken;
    private String name;
    private String nickname;
    private String userrole = "ROLE_USER";
    private String gender;
    private String profilePicture;
    private String birthDate;
    private Boolean active = true;

}