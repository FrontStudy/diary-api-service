package com.yuk.wazzangstudyrestapi1.services;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.yuk.wazzangstudyrestapi1.domains.ImageInfo;
import com.yuk.wazzangstudyrestapi1.dtos.file.ImageInfoResponseDto;
import com.yuk.wazzangstudyrestapi1.repositorys.ImageInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    final private ImageInfoRepository imageInfoRepository;

    public ArrayList<ImageInfoResponseDto> uploadImages(MultipartFile[] imgs) {
        ArrayList<ImageInfoResponseDto> rtnPaths = new ArrayList<>();
        Instant instant = Instant.now();

        for (MultipartFile mf : imgs) {
            String fileName = instant.getEpochSecond() + "_" + mf.getOriginalFilename();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(mf.getContentType());
            try {
                InputStream inputStream = mf.getInputStream();
                PutObjectRequest putObj = new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead);
                amazonS3Client.putObject(putObj);
                String uploadUrl = amazonS3Client.getUrl(bucketName, fileName).toString();

                ImageInfo row = new ImageInfo();
                row.setFileName(fileName);
                row.setStoragePath(uploadUrl);
                row.setCreatedDate(LocalDateTime.now());
                row.setModifiedDate(LocalDateTime.now());

                row = imageInfoRepository.save(row);

                ImageInfoResponseDto result = ImageInfoResponseDto.builder()
                        .id(row.getId())
                        .storagePath(row.getStoragePath())
                        .build();

                rtnPaths.add(result);
            } catch (Exception e) {
                System.out.println("Exception e : " + e);
            }
        }

        return rtnPaths;
    }

    public ArrayList<ImageInfoResponseDto> getImages(String imgs) {
        if(imgs==null || imgs.isEmpty()) {
            return null;
        } else {
            String[] arrImgs = imgs.split(",");
            ArrayList<Long> imgIds = new ArrayList<>();
            for(String val : arrImgs) {
                try {
                    imgIds.add(Long.parseLong(val.trim()));
                } catch (Exception e) {
                    System.out.println("Exception e : " + e);
                }

            }
            ArrayList<ImageInfo> rows = imageInfoRepository.findByIdIn(imgIds);

            return rows.stream()
                    .map(ImageInfoResponseDto::from)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
    }
}