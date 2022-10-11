package com.example.shyneeds_be.global.service;

import com.example.shyneeds_be.domain.travel_package.model.dto.response.TravelPackageResponseDto;
import com.example.shyneeds_be.domain.travel_package.model.entitiy.TravelPackage;
import com.example.shyneeds_be.domain.travel_package.repository.TravelPackageRepository;
import com.example.shyneeds_be.global.model.dto.request.CurationRequestDto;
import com.example.shyneeds_be.global.model.dto.request.MainRequestDto;
import com.example.shyneeds_be.global.model.dto.response.MainBannerResponseDto;
import com.example.shyneeds_be.global.model.dto.response.MainTravelPackage;
import com.example.shyneeds_be.global.model.dto.response.MainTravelPackageResponseDto;
import com.example.shyneeds_be.global.model.dto.response.MainResponseDto;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class MainService {


    private final TravelPackageRepository travelPackageRepository;

    /*
    메인 화면
    - 메인 배너
    - 큐레이션 (따로 호출)
    - 그룹별
    - 지역별
    - 테마별
    - 후기 (추후 작업)
     */
    public ApiResponseDto<MainResponseDto> getMain(MainRequestDto mainRequestDto){
        try {

            List<MainBannerResponseDto> mainBannerList = this.getMainBanner();
            Map<String, List<MainTravelPackageResponseDto>> mainTravelPackageMap = new HashMap<String, List<MainTravelPackageResponseDto>>();
            for(String category : mainRequestDto.getCategoryList()){
                mainTravelPackageMap.put(category, this.getTravelPackageList(category) );
            }

            MainResponseDto mainResponseDto = MainResponseDto.builder()
                    .mainBannerList(mainBannerList)
                    .mainCategoryPackageList(mainTravelPackageMap)
                    .build();

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "조회에 성공했습니다.", mainResponseDto);
        } catch (Exception e) {
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "조회에 실패했습니다." + e.getMessage());
        }
    }

    /*------------------------------ 메인 배너 상품 리스트 출력 -----------------------------------------*/
    private List<MainBannerResponseDto> getMainBanner(){
        try{

            return travelPackageRepository.findByMainBanner().stream().map(this::responseMainBanner).toList();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private MainBannerResponseDto responseMainBanner(TravelPackage travelPackage) {
        String imageDir = "https://shyneeds.s3.ap-northeast-2.amazonaws.com/package/"+
                travelPackage.getTitle()+"/main/"+travelPackage.getMainImage();
        return MainBannerResponseDto.builder()
                .id(travelPackage.getId())
                .title(travelPackage.getTitle())
                .imageUrl(imageDir)
                .keyword(travelPackage.getSearchKeyword())
                .build();
    }

    /*------------------------------  메인 카테고리별 리스트 출력 -----------------------------------------*/
    private List<MainTravelPackageResponseDto> getTravelPackageList(String category){
        try{
            return travelPackageRepository.findByCategory(category).stream().map(this::responseMainTravelResponseDto).toList();
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private MainTravelPackageResponseDto responseMainTravelResponseDto(MainTravelPackage mainTravelPackage){
        String imageDir = "https://shyneeds.s3.ap-northeast-2.amazonaws.com/package/"+
                mainTravelPackage.getTitle()+"/main/"+mainTravelPackage.getMainImage();
        String tagUrl = "https://shyneeds.s3.ap-northeast-2.amazonaws.com/design/badge/" + mainTravelPackage.getSubCategoryTitle() +".svg";
        return MainTravelPackageResponseDto.builder()
                .id(mainTravelPackage.getId())
                .title(mainTravelPackage.getTitle())
                .imageUrl(imageDir)
                .price(mainTravelPackage.getPrice())
                .summary(mainTravelPackage.getSummary())
                .tag((mainTravelPackage.getCategoryTitle().contains("지역별상품") ?  null : tagUrl))
                .keyword(mainTravelPackage.getKeyword())
                .build();
    }


    /*------------------------------ 큐레이션 결과 -----------------------------------------*/
    public ApiResponseDto<List<MainTravelPackageResponseDto>> getResultCuration(CurationRequestDto curationRequestDto) {
        try {

            List<MainTravelPackageResponseDto> curationResultPackageList = new ArrayList<>();
            String age = curationRequestDto.getAge();
            String accompany = curationRequestDto.getAccompany();
            String tendency = curationRequestDto.getTendency();
            String religion = curationRequestDto.getReligion();
            String country = curationRequestDto.getCountry();
            String theme = curationRequestDto.getTheme();
            if(age != null || accompany != null ||  tendency != null || religion != null
                    || country != null || theme != null
            ){
                curationResultPackageList = travelPackageRepository.findByCuration(
                        age,
                        accompany,
                        tendency,
                        religion,
                        country,
                        theme).stream().map(this::responseMainTravelResponseDto).toList();
            }


            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "조회에 성공했습니다.", curationResultPackageList);
        } catch (Exception e) {
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "조회에 실패했습니다." + e.getMessage());

        }
    }
}
