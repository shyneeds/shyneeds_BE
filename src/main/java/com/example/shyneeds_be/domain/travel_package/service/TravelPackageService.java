package com.example.shyneeds_be.domain.travel_package.service;

import com.example.shyneeds_be.domain.category.model.entity.Category;
import com.example.shyneeds_be.domain.category.model.entity.SubCategory;
import com.example.shyneeds_be.domain.category.model.response.CategoryResponseDto;
import com.example.shyneeds_be.domain.category.model.response.SubCategoryResponseDto;
import com.example.shyneeds_be.domain.category.repository.CategoryRepository;
import com.example.shyneeds_be.domain.travel_package.model.dto.request.TravelPackageRegisterRequestDto;
import com.example.shyneeds_be.domain.travel_package.model.dto.response.TravelPackageResponseDto;
import com.example.shyneeds_be.domain.travel_package.model.entitiy.TravelPackage;
import com.example.shyneeds_be.domain.travel_package.repository.TravelPackageRepository;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import com.example.shyneeds_be.global.network.s3.ItemS3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class TravelPackageService {

    private final ItemS3Uploader itemS3Uploader;
    private final TravelPackageRepository travelPackageRepository;
    private final CategoryRepository categoryRepository;

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

    /*--------------------------------[어드민] 상품 리스트 조회------------------------------------ */
    public ApiResponseDto<List<TravelPackageResponseDto>> getAdminPackageList() {
       try{

           List<TravelPackageResponseDto> travelPackageResponseDtoList = travelPackageRepository.findAll().stream().map(this::response).toList();

           return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "조회에 성공했습니다.", travelPackageResponseDtoList);
       } catch (Exception e){
           return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "조회에 실패했습니다." + e.getMessage());
       }
    }

    /*--------------------------------[어드민] 상품 상세 조회------------------------------------ */
    public ApiResponseDto<TravelPackageResponseDto> getAdminPackage(Long id) {
        try{
            Optional<TravelPackage> optionalTravelPackage = travelPackageRepository.findById(id);

            if(optionalTravelPackage.isEmpty()){
                return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "해당 상품을 찾을 수 없습니다.");
            } else {
                TravelPackageResponseDto travelPackageResponseDto = response(optionalTravelPackage.get());
                return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "조회에 성공했습니다.", travelPackageResponseDto);
            }


        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "조회에 실패했습니다." + e.getMessage());
        }
    }

    /*--------------------------------[어드민] 상품 삭제 - soft delete------------------------------------ */
    public ApiResponseDto deletedPackage(Long id) {
        try{

            // 상품 조회
            Optional<TravelPackage> optionalTravelPackage = travelPackageRepository.findById(id);
            if(optionalTravelPackage.isEmpty()){
                return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "해당 상품을 찾을 수 없습니다.");
            } else {

                // 삭제 및 저장
                TravelPackage deletedTravelPackage = optionalTravelPackage.get().deleted();
                travelPackageRepository.save(deletedTravelPackage);

                return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "상품 삭제에 성공했습니다.");
            }
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "상품 삭제에 실패했습니다." + e.getMessage());
        }
    }






    private TravelPackageResponseDto response(TravelPackage travelPackage){

        List<CategoryResponseDto> categoryResponseDtoList = new ArrayList<>();

        StringTokenizer st = new StringTokenizer(travelPackage.getCategoryIds(), ",");
        while(st.hasMoreElements()){
            Optional<Category> optionalCategory = categoryRepository.findById(Long.valueOf(st.nextToken()));
            if(optionalCategory.isPresent() && optionalCategory != null){
                categoryResponseDtoList.add(responseCategoryResponse(optionalCategory.get()));
            }
        }



        String imageDir = "https://shyneeds.s3.ap-northeast-2.amazonaws.com/package/"+
                travelPackage.getTitle();

        List<String> descriptionImageUrlList = new ArrayList<>();
        descriptionImageUrlList.add(imageDir + "/description/" + travelPackage.getDescriptionImage());

        // search keyword
        List<String> searchKeywordList = new ArrayList<>();
        StringTokenizer skSt = new StringTokenizer(travelPackage.getSearchKeyword());
        while (skSt.hasMoreElements()) {
            searchKeywordList.add(skSt.nextToken());
        }

        return TravelPackageResponseDto.builder()
                .id(travelPackage.getId())
                .title(travelPackage.getTitle())
                .categoryResponseDtoList(categoryResponseDtoList)
                .mainImage(imageDir +"/main/"
                        +travelPackage.getMainImage())
                .descriptionImage(descriptionImageUrlList)
                .price(travelPackage.getPrice())
                .summary(travelPackage.getSummary())
                .requiredOptionName(travelPackage.getRequiredOptionName())
                .requiredOptionValues(travelPackage.getRequiredOptionValues())
                .optionalName(travelPackage.getOptionalName())
                .optionalValues(travelPackage.getOptionalValues())
                .flightInfo(travelPackage.getFlightInfo())
                .soldoutFlg(travelPackage.isSoldoutFlg())
                .dispFlg(travelPackage.isDispFlg())
                .createdAt(travelPackage.getCreatedAt())
                .updatedAt(travelPackage.getUpdatedAt())
                .searchKeyword(searchKeywordList)
                .build();
    }

    private String removeBracket(List list){
        String listString = list.toString();
        listString = listString.replaceAll("\\[", "");
        listString = listString.replaceAll("\\]", "");
        listString = listString.replaceAll(" ", "");

        return listString;
    }

    private CategoryResponseDto responseCategoryResponse(Category category){

        return CategoryResponseDto.builder()
                .id(category.getId())
                .title(category.getTitle())
                .subCategoryResponseDtoList(null)
                .dispFlg(category.isDispFlg())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();

    }



}