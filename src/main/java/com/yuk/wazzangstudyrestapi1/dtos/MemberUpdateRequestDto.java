package com.yuk.wazzangstudyrestapi1.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateRequestDto {
    private String profilePicture;
    private String nickname;
    private boolean isActive = true;
}
