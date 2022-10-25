package com.example.shyneeds_be.global.model.dto.response;

import com.example.shyneeds_be.domain.travel_package.model.entitiy.TravelPackage;

public interface MainTravelPackage {

    String getCategoryTitle(); // category title
    String getSubCategoryTitle(); // subCategory title
    Long getId();
    String getTitle();
    String getMainImage();
    String getSummary();
    String getPrice();
    String getKeyword();

}
