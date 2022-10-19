package com.example.shyneeds_be.domain.cart.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddCartRequestDto {

    List<AddCartPackageRequestDto> addCartPackageRequestList;


}
