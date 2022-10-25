package com.example.shyneeds_be.global.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pagination {

    private Integer totalPages;
    private Long totalElements;
    private Integer currentPage;
    private Integer currentElements;

    public static <T> Pagination getPagination(Page<T> entityList){
        return Pagination.builder()
                .totalPages(entityList.getTotalPages())
                .totalElements(entityList.getTotalElements())
                .currentElements(entityList.getNumberOfElements())
                .currentPage(entityList.getNumber()+1)
                .build();
    }

}
