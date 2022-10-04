package com.example.shyneeds_be.global.network.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class ItemS3Uploader {

    private final AmazonS3Client amazonS3Client;

    // 로컬로 파일 업로드
    public String uploadLocal(MultipartFile multipartFile, String bucketDir){
        try{
            File uploadFile = convert(multipartFile).orElseThrow(() -> new IllegalArgumentException("error : MultipartFile -> File convert fail"));
            return uploadS3(uploadFile, bucketDir);
        } catch(Exception e){

            log.error(e.getMessage());
        }

        return null;
    }

    // 로컬에 파일 업로드 하기 - multipartfile to file
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());

        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }

            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    // S3로 파일 업로드하기 - 이미지 이름 생성 및 로컬에 저장된 이미지 지우기
    private String uploadS3(File uploadFile, String bucketDir){
        try {
            String[] splitFileName = uploadFile.getName().split("\\.");
            String imageType = splitFileName[splitFileName.length-1];

            // 이미지 이름 생성
            String fileName = splitFileName[0] + "_" + UUID.randomUUID() + "." + imageType; // S3에 저장될 파일 이름

            String uploadImageUrl = putS3(uploadFile, bucketDir, fileName); //s3로 업로드


            // 로컬 저장 파일 삭제
            removeNewFile(uploadFile);

            return uploadImageUrl;
        } catch (Exception e){
            log.error(e.getMessage());
        }

        return null;
    }

    // S3로 파일 업로드하기 - S3 객체로 업로드
    private String putS3(File uploadFile, String bucketDir, String fileName) {
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucketDir, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
            return fileName;
        } catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }

    // 로컬 저장된 이미지 지우기
    private void removeNewFile(File targetFile){
        try {
            targetFile.delete();
        } catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
