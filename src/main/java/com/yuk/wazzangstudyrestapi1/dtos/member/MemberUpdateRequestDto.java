package com.yuk.wazzangstudyrestapi1.dtos.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateRequestDto {
    private String profilePicture;
    private String nickname;
    private String name;
    private String birthDate;
}
