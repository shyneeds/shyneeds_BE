package com.example.shyneeds_be.domain.travel_package.service;

import com.example.shyneeds_be.domain.category.model.entity.Category;
import com.example.shyneeds_be.domain.category.model.response.CategoryResponseDto;
import com.example.shyneeds_be.domain.category.repository.CategoryRepository;
import com.example.shyneeds_be.domain.travel_package.model.dto.request.PackageOptionRequestDto;
import com.example.shyneeds_be.domain.travel_package.model.dto.request.TravelPackageRegisterRequestDto;
import com.example.shyneeds_be.domain.travel_package.model.dto.response.DetailPackageResponseDto;
import com.example.shyneeds_be.domain.travel_package.model.dto.response.PackageOptionResponseDto;
import com.example.shyneeds_be.domain.travel_package.model.dto.response.TravelPackageResponseDto;
import com.example.shyneeds_be.domain.travel_package.model.entitiy.PackageOption;
import com.example.shyneeds_be.domain.travel_package.model.entitiy.TravelPackage;
import com.example.shyneeds_be.domain.travel_package.repository.PackageOptionRepository;
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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class TravelPackageService {

    private final ItemS3Uploader itemS3Uploader;
    private final TravelPackageRepository travelPackageRepository;
    private final CategoryRepository categoryRepository;
    private final PackageOptionRepository packageOptionRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // ?????? ?????? ??????
    public ApiResponseDto<DetailPackageResponseDto> getPackageDetail(Long id){
        try{

            // ?????? ??????
            TravelPackage travelPackage = travelPackageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("???????????? ????????? ????????????."));
            TravelPackageResponseDto travelPackageResponseDto = response(travelPackage);

            // ?????? ?????? ?????????
            List<TravelPackageResponseDto> relatedPackageList = travelPackageRepository.findRelatedPackageList(id).stream().map(this::response).toList();
            DetailPackageResponseDto detailPackageResponseDto = DetailPackageResponseDto.builder()
                    .travelPackageResponseDto(travelPackageResponseDto)
                    .relatedPackageList(relatedPackageList)
                    .build();


            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "????????? ??????????????????.", detailPackageResponseDto);
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "????????? ??????????????????." + e.getMessage());
        }
    }



    /*
    ADMIN ?????? : ?????? ??????
    */
    public ApiResponseDto register(TravelPackageRegisterRequestDto travelPackageRegisterRequestDto, MultipartFile mainImage, List<MultipartFile> descriptionImageList){


        try{
            if (mainImage != null) {

                if(travelPackageRegisterRequestDto != null) {
                    // ?????? ????????? ?????? - S3??? ????????? ????????? && ?????? ???????????? url ??????
                    String title = travelPackageRegisterRequestDto.getTitle();


                    String mainImageUrl = uploadS3MainImage(mainImage, title);
                    String descriptionImageUrl = uploadS3DescriptionImage(descriptionImageList, title);

                    // ????????? ??????
                    TravelPackage travelPackage = TravelPackage.builder()
                            .title(title)
                            .categoryIds(removeBracket(travelPackageRegisterRequestDto.getCategoryIds()))
                            .subCategoryIds(removeBracket(travelPackageRegisterRequestDto.getSubCategoryIds()))
                            .thirdCategoryIds(removeBracket(travelPackageRegisterRequestDto.getThirdCategoryIds()))
                            .mainImage(mainImageUrl)
                            .descriptionImage(descriptionImageUrl)
                            .price(travelPackageRegisterRequestDto.getPrice())
                            .summary(travelPackageRegisterRequestDto.getSummary())
                            .soldoutFlg(travelPackageRegisterRequestDto.isSoldoutFlg())
                            .dispFlg(travelPackageRegisterRequestDto.isDispFlg())
                            .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                            .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                            .searchKeyword(removeBracket(travelPackageRegisterRequestDto.getSearchKeyword()))
                            .build();

                    TravelPackage saveTravelPackage = travelPackageRepository.save(travelPackage);

                    // ????????? ?????? ??????
                    if(travelPackageRegisterRequestDto.getPackageOptionRequestDtoList() != null){
                        List<PackageOptionRequestDto> packageOptionRequestDtoList = travelPackageRegisterRequestDto.getPackageOptionRequestDtoList();

                        for (PackageOptionRequestDto packageOptionRequestDto : packageOptionRequestDtoList) {
                            PackageOption savePackageOption = PackageOption.builder()
                                    .title(packageOptionRequestDto.getTitle())
                                    .optionValue(packageOptionRequestDto.getOptionValue())
                                    .travelPackage(saveTravelPackage)
                                    .price(packageOptionRequestDto.getPrice())
                                    .optionFlg(packageOptionRequestDto.isOptionFlg())
                                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                                    .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                                    .build();

                            packageOptionRepository.save(savePackageOption);
                        }
                    }


                } else {
                    return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "??????????????? ???????????? ????????? ??????????????????.");
                }
            }
            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "????????? ??????????????????.");
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "????????? ??????????????????." + e.getMessage());
        }
    }


    // Main ????????? ??????. bucketDir = ?????? ??????
    private String uploadS3MainImage(MultipartFile mainImage, String bucketDir){
        return itemS3Uploader.uploadLocal(mainImage, bucket+"/package/"+bucketDir+"/main");
    }

    // ?????? ????????? ??????
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

    /*--------------------------------[?????????] ?????? ????????? ??????------------------------------------ */
    public ApiResponseDto<List<TravelPackageResponseDto>> getAdminPackageList(String title) {
       try{

           List<TravelPackageResponseDto> travelPackageResponseDtoList = new ArrayList<>();
           if(title.equals("all")) {
               travelPackageResponseDtoList = travelPackageRepository.findAll().stream().map(this::response).toList();
           } else {
               travelPackageResponseDtoList = travelPackageRepository.findSearchPackageList(title).stream().map(this::response).toList();
           }

           return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "????????? ??????????????????.", travelPackageResponseDtoList);
       } catch (Exception e){
           return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "????????? ??????????????????." + e.getMessage());
       }
    }

    /*--------------------------------[?????????] ?????? ?????? ??????------------------------------------ */
    public ApiResponseDto<TravelPackageResponseDto> getAdminPackage(Long id) {
        try{
            Optional<TravelPackage> optionalTravelPackage = travelPackageRepository.findById(id);

            if(optionalTravelPackage.isEmpty()){
                return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "?????? ????????? ?????? ??? ????????????.");
            } else {
                TravelPackageResponseDto travelPackageResponseDto = response(optionalTravelPackage.get());
                return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "????????? ??????????????????.", travelPackageResponseDto);
            }


        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "????????? ??????????????????." + e.getMessage());
        }
    }

    /*--------------------------------[?????????] ?????? ?????? - soft delete------------------------------------ */
    public ApiResponseDto deletedPackage(Long id) {
        try{

            // ?????? ??????
            Optional<TravelPackage> optionalTravelPackage = travelPackageRepository.findById(id);
            if(optionalTravelPackage.isEmpty()){
                return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "?????? ????????? ?????? ??? ????????????.");
            } else {

                // ?????? ??? ??????
                TravelPackage deletedTravelPackage = optionalTravelPackage.get().deleted();
                travelPackageRepository.save(deletedTravelPackage);

                return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "?????? ????????? ??????????????????.");
            }
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "?????? ????????? ??????????????????." + e.getMessage());
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


        // package option
        Map<String, List<PackageOptionResponseDto>> packageOptionMap = responsePackageOptionMap(travelPackage.getPackageOptionList());
        return TravelPackageResponseDto.builder()
                .id(travelPackage.getId())
                .title(travelPackage.getTitle())
                .categoryResponseDtoList(categoryResponseDtoList)
                .mainImage(imageDir +"/main/"
                        +travelPackage.getMainImage())
                .descriptionImage(descriptionImageUrlList)
                .price(travelPackage.getPrice())
                .summary(travelPackage.getSummary())
                .packageOptionResponseDto(packageOptionMap)
                .soldoutFlg(travelPackage.isSoldoutFlg())
                .dispFlg(travelPackage.isDispFlg())
                .createdAt(travelPackage.getCreatedAt())
                .updatedAt(travelPackage.getUpdatedAt())
                .searchKeyword(searchKeywordList)
                .build();
    }


    private Map<String, List<PackageOptionResponseDto>> responsePackageOptionMap(List<PackageOption> packageOptionList){

        Map<String, List<PackageOptionResponseDto>> packageOptionMap =
                this.addQuotationTitle(packageOptionList).stream().map(this::responsePackageOptionResponseDto).toList().stream().collect(Collectors.groupingBy(PackageOptionResponseDto::getTitle));



        return packageOptionMap;

    }

    public PackageOptionResponseDto responsePackageOptionResponseDto(PackageOption packageOption){
       return PackageOptionResponseDto.builder()
                .id(packageOption.getId())
                .title(packageOption.getTitle())
                .optionValue(packageOption.getOptionValue())
                .price(packageOption.getPrice())
                .optionFlg(packageOption.isOptionFlg())
                .createdAt(packageOption.getCreatedAt())
                .updatedAt(packageOption.getUpdatedAt())
                .build();
    }


    // ???????????? "" ?????? ??? ????????? ???????????? ??????
    private List<PackageOption> addQuotationTitle(List<PackageOption> packageOptionList) {


       return  packageOptionList.stream().map(packageOption -> {

            StringBuilder sb = new StringBuilder();
            sb.append("\'");
            sb.append(packageOption.getTitle());
            sb.append("\'");
            String quotationTitle = sb.toString();

            return PackageOption.builder()
                    .id(packageOption.getId())
                    .travelPackage(packageOption.getTravelPackage())
                    .title(quotationTitle)
                    .optionValue(packageOption.getOptionValue())
                    .price(packageOption.getPrice())
                    .optionFlg(packageOption.isOptionFlg())
                    .createdAt(packageOption.getCreatedAt())
                    .updatedAt(packageOption.getUpdatedAt())
                    .build();
        }).toList();


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
