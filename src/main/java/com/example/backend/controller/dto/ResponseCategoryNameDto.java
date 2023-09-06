package com.example.backend.controller.dto;

import com.example.backend.domain.Category;
import lombok.Data;

@Data
public class ResponseCategoryNameDto {
    Long id;
    String categoryName;

    public ResponseCategoryNameDto(Category category){
        this.id = category.getId();
        this.categoryName = category.getName();
    }
}
