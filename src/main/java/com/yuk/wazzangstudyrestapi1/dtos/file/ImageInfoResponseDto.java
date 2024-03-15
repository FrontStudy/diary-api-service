package com.yuk.wazzangstudyrestapi1.dtos.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yuk.wazzangstudyrestapi1.domains.Diary;
import com.yuk.wazzangstudyrestapi1.domains.ImageInfo;
import com.yuk.wazzangstudyrestapi1.dtos.BaseTimeEntity;
import com.yuk.wazzangstudyrestapi1.dtos.diary.DiaryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ImageInfoResponseDto extends BaseTimeEntity {
    private Long id;

    private String storagePath;
    private String fileName;

    public ImageInfoResponseDto(Long id, String storagePath, String fileName, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        super(createdDate, modifiedDate);
        this.id = id;
        this.storagePath = storagePath;
        this.fileName = fileName;
    }

    public static ImageInfoResponseDto from(ImageInfo imageInfo) {
        return new ImageInfoResponseDto(
                imageInfo.getId(),
                imageInfo.getStoragePath(),
                imageInfo.getFileName(),
                imageInfo.getCreatedDate(),
                imageInfo.getModifiedDate()
        );
    }
}