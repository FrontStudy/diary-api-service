package com.yuk.wazzangstudyrestapi1.dtos.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yuk.wazzangstudyrestapi1.dtos.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MemberAdminResponseDto extends BaseTimeEntity {

    private Long id;
    private String profilePicture;
    private String nickname;
    private String birthDate;
    private String name;
    private String gender;
    private String email;
    private LocalDateTime withdrawalDate;
    private boolean active;
    private Long diaryCount;
    private Long commentCount;

    public MemberAdminResponseDto(LocalDateTime createdDate, LocalDateTime modifiedDate, Long id, String profilePicture, String nickname, String birthDate, String name, String gender, String email, LocalDateTime withdrawalDate, boolean active, Long diaryCount, Long commentCount) {
        super(createdDate, modifiedDate);
        this.id = id;
        this.profilePicture = profilePicture;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.withdrawalDate = withdrawalDate;
        this.active = active;
        this.diaryCount = diaryCount;
        this.commentCount = commentCount;
    }
}
