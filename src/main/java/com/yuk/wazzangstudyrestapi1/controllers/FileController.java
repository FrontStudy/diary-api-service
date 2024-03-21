package com.yuk.wazzangstudyrestapi1.controllers;

import com.yuk.wazzangstudyrestapi1.dtos.ResponseDto;
import com.yuk.wazzangstudyrestapi1.dtos.file.ImageInfoRequestDto;
import com.yuk.wazzangstudyrestapi1.dtos.file.ImageInfoResponseDto;
import com.yuk.wazzangstudyrestapi1.exceptions.CustomException;
import com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode;
import com.yuk.wazzangstudyrestapi1.security.SecurityUserDetail;
import com.yuk.wazzangstudyrestapi1.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class FileController {
    final private FileService fileService;

    @PostMapping(value="/svc/upImages")
    public ResponseDto upImages(@RequestParam MultipartFile[] imgs) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(imgs.length < 1) throw new CustomException(ErrorCode.EMPTY_REQUEST);

        ArrayList<ImageInfoResponseDto> result = fileService.uploadImages(imgs);
        if(!result.isEmpty()) {
            return ResponseDto.builder()
                    .status("success")
                    .data(result)
                    .build();
        } else {
            return ResponseDto.builder()
                    .status("fail")
                    .emsg("error in process")
                    .build();
        }
    }

    @GetMapping(value="/svc/getImages")
    public ResponseDto getImages(@RequestParam String fileName) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(fileName == null || fileName.isEmpty()) throw new CustomException(ErrorCode.EMPTY_REQUEST);

        ArrayList<ImageInfoResponseDto> result = fileService.getImages(fileName);

        if(!result.isEmpty()) {
            return ResponseDto.builder()
                    .status("success")
                    .data(result)
                    .build();
        } else {
            return ResponseDto.builder()
                    .status("fail")
                    .emsg("error in process")
                    .build();
        }
    }

    @GetMapping(value="/pub/getImages")
    public ResponseDto getImagesPub(@RequestParam String fileName) {
        if(fileName == null || fileName.isEmpty()) throw new CustomException(ErrorCode.EMPTY_REQUEST);

        ArrayList<ImageInfoResponseDto> result = fileService.getImages(fileName);

        if(!result.isEmpty()) {
            return ResponseDto.builder()
                    .status("success")
                    .data(result)
                    .build();
        } else {
            return ResponseDto.builder()
                    .status("fail")
                    .emsg("error in process")
                    .build();
        }
    }
}
