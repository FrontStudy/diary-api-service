package com.yuk.wazzangstudyrestapi1.dtos.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MemberDetailInfoDto {
    private Long id;
    private String email;
    private String profilePicture;
    private String nickname;
    private String name;
    private String birthDate;
    private String gender;

    private Long diaryCount;
    private Long followerCount;
    private Long followingCount;
}
