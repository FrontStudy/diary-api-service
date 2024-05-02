package com.yuk.wazzangstudyrestapi1.dtos.diary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yuk.wazzangstudyrestapi1.domains.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DiaryDetailsResponseDto extends DiaryResponseDto {

    private Long readCount;
    private Long commentCount;
    private Long likeCount;
    private Boolean isLiked;
    private Boolean isBookmarked;

    private Long authorId;
    private String authorEmail;
    private String authorName;
    private String authorNickname;
    private String authorProfileUrl;

    public void setAuthor(Member member) {
        this.authorId = member.getId();
        this.authorEmail = member.getEmail();
        this.authorName = member.getName();
        this.authorNickname = member.getNickname();
        this.authorProfileUrl = member.getProfilePicture();
    }
}
