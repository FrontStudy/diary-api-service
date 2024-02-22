package com.yuk.wazzangstudyrestapi1.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateRequestDto {
    private Long id;
    private String profilePicture;
    private String nickname;
    private Boolean active;
}
