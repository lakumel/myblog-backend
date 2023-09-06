package com.example.backend.controller.dto;

import com.example.backend.domain.Category;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ResponseCategoryDto {
    private Long id;
    private String name;
    private List<ResponsePostDto> postDtoList;

    public ResponseCategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.postDtoList = category.getPosts().stream()
                .map(ResponsePostDto::new)
                .collect(Collectors.toList());
    }
}
