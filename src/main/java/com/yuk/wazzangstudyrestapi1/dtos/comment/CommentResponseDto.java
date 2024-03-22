package com.yuk.wazzangstudyrestapi1.dtos.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yuk.wazzangstudyrestapi1.domains.Comment;
import com.yuk.wazzangstudyrestapi1.dtos.BaseTimeEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommentResponseDto extends BaseTimeEntity {

    private Long id;
    private Long memberId;
    private Long diaryId;
    private String content;
    private Boolean active = true;

    public CommentResponseDto(LocalDateTime createdDate, LocalDateTime modifiedDate, Long id, Long memberId, Long diaryId, String content, Boolean active) {
        super(createdDate, modifiedDate);
        this.id = id;
        this.memberId = memberId;
        this.diaryId = diaryId;
        this.content = content;
        this.active = active;
    }

    public static CommentResponseDto from(Comment comment) {
        return new CommentResponseDto(
                comment.getCreatedDate(),
                comment.getModifiedDate(),
                comment.getId(),
                comment.getMemberId(),
                comment.getDiaryId(),
                comment.getContent(),
                comment.getActive()
        );
    }
}
