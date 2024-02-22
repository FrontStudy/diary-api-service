package com.yuk.wazzangstudyrestapi1.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yuk.wazzangstudyrestapi1.domains.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MemberResponseDto {

    private Long id;
    private String profilePicture;
    private String nickname;
    private String birthDate;
    private String name;
    private String gender;
    private String email;
    private LocalDateTime withdrawalDate;
    private boolean active;

    public MemberResponseDto(Long id, String profilePicture, String nickname, String birthDate,
                             String name, String gender, String email, LocalDateTime withdrawalDate,
                             boolean active) {
        this.id = id;
        this.profilePicture = profilePicture;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.withdrawalDate = withdrawalDate;
        this.active = active;
    }

    // 여기에 Member 엔티티를 MemberResponseDto로 변환하는 스태틱 메소드를 추가할 수 있습니다.
    public static MemberResponseDto from(Member member) {
        return new MemberResponseDto(
                member.getId(),
                member.getProfilePicture(),
                member.getNickname(),
                member.getBirthDate(),
                member.getName(),
                member.getGender(),
                member.getEmail(),
                member.getWithdrawalDate(),
                member.getActive()
        );
    }
}
