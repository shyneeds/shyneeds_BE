package com.example.shyneeds_be.global.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseDto<T> {

    // api 상태 응답 코드
    private Integer statusCode;
    
    // message
    private String message;
    
    // data
    private T data;
    
    // Paging
    private Pagination pagination;

    //Data가 없는 ApiResponseDto
    public static <T> ApiResponseDto<T> of(Integer responseStatusCode, String  responseMessage ){
        return (ApiResponseDto<T>) ApiResponseDto.builder()
                .statusCode(responseStatusCode)
                .message(responseMessage)
                .build();
    }

    //Data가 있는 ApiResponseDto
    public static <T> ApiResponseDto<T> of(Integer responseStatusCode, String  responseMessage, T data ){
        return (ApiResponseDto<T>) ApiResponseDto.builder()
                .statusCode(responseStatusCode)
                .message(responseMessage)
                .data(data)
                .build();
    }

    //Pagination ApiResponseDto
    public static <T> ApiResponseDto<T> of(Integer responseStatusCode, String  responseMessage, T data, Pagination pagination){
        return (ApiResponseDto<T>)  ApiResponseDto.builder()
                .statusCode(responseStatusCode)
                .message(responseMessage)
                .data(data)
                .pagination(pagination)
                .build();
    }
}
