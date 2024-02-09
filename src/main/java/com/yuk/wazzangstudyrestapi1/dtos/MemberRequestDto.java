package com.yuk.wazzangstudyrestapi1.dtos;

import com.yuk.wazzangstudyrestapi1.domains.Member;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private boolean isActive = true;

}