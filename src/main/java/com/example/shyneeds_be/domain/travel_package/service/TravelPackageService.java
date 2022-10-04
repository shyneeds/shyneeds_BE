package com.example.shyneeds_be.domain.travel_package.service;

import com.example.shyneeds_be.domain.travel_package.model.dto.request.TravelPackageRegisterRequestDto;
import com.example.shyneeds_be.domain.travel_package.model.entitiy.TravelPackage;
import com.example.shyneeds_be.domain.travel_package.repository.TravelPackageRepository;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import com.example.shyneeds_be.global.network.s3.ItemS3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TravelPackageService {

    private final ItemS3Uploader itemS3Uploader;
    private final TravelPackageRepository travelPackageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /*
    ADMIN 기능 : 상품 등록
    */
    public ApiResponseDto register(TravelPackageRegisterRequestDto travelPackageRegisterRequestDto, MultipartFile mainImage, List<MultipartFile> descriptionImageList){

        try{
            if (mainImage != null) {

                if(travelPackageRegisterRequestDto != null) {
                    // 상품 이미지 저장 - S3에 이미지 업로드 && 상품 테이블에 url 저장
                    String title = travelPackageRegisterRequestDto.getTitle();


                    String mainImageUrl = uploadS3MainImage(mainImage, title);
                    String descriptionImageUrl = uploadS3DescriptionImage(descriptionImageList, title);

                    TravelPackage travelPackage = TravelPackage.builder()
                            .title(title)
                            .categoryIds(removeBracket(travelPackageRegisterRequestDto.getCategoryIds()))
                            .subCategoryIds(removeBracket(travelPackageRegisterRequestDto.getSubCategoryIds()))
                            .thirdCategoryIds(removeBracket(travelPackageRegisterRequestDto.getThirdCategoryIds()))
                            .mainImage(mainImageUrl)
                            .descriptionImage(descriptionImageUrl)
                            .price(travelPackageRegisterRequestDto.getPrice())
                            .summary(travelPackageRegisterRequestDto.getSummary())
                            .requiredOptionName(travelPackageRegisterRequestDto.getRequiredOptionName())
                            .requiredOptionValues(travelPackageRegisterRequestDto.getRequiredOptionValues())
                            .optionalName(travelPackageRegisterRequestDto.getOptionalName())
                            .optionalValues(travelPackageRegisterRequestDto.getOptionalValues())
                            .flightInfo(travelPackageRegisterRequestDto.getFlightInfo())
                            .soldoutFlg(travelPackageRegisterRequestDto.isSoldoutFlg())
                            .dispFlg(travelPackageRegisterRequestDto.isDispFlg())
                            .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                            .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                            .searchKeyword(removeBracket(travelPackageRegisterRequestDto.getSearchKeyword()))
                            .build();

                    travelPackageRepository.save(travelPackage);


                } else {
                    return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "등록정보가 부족하여 등록에 실패했습니다.");
                }
            }
            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "등록에 성공했습니다.");
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "등록에 실패했습니다." + e.getMessage());
        }
    }


    // Main 이미지 저장. bucketDir = 도시 이름
    private String uploadS3MainImage(MultipartFile mainImage, String bucketDir){
        return itemS3Uploader.uploadLocal(mainImage, bucket+"/package/"+bucketDir+"/main");
    }

    // 상세 이미지 저장
    // multpartfile -> file -> url list -> string
    private String uploadS3DescriptionImage(List<MultipartFile> descriptionImageList, String bucketDir){
        if(!descriptionImageList.isEmpty() || descriptionImageList != null){

            return removeBracket(
                    descriptionImageList.stream()
                    .map(multipartFile ->
                            itemS3Uploader.uploadLocal(multipartFile, bucket + "/package/" + bucketDir + "/description")
                    )
                    .collect(Collectors.toList()));

        }
        return null;
    }

    private String removeBracket(List list){
        String listString = list.toString();
        listString = listString.replaceAll("\\[", "");
        listString = listString.replaceAll("\\]", "");

        return listString;
    }


}
