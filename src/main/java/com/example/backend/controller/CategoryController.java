package com.example.backend.controller;

import com.example.backend.controller.dto.ResponseCategoryDto;
import com.example.backend.controller.dto.ResponseCategoryNameDto;
import com.example.backend.domain.Category;
import com.example.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping("")
    public ResponseEntity<List<ResponseCategoryNameDto>> getAllCategory(){
        List<Category> allCategory = categoryService.getAllCategory();
        List<ResponseCategoryNameDto> categoryNameDtoList
                = allCategory.stream().map(ResponseCategoryNameDto::new).collect(Collectors.toList());

        return new ResponseEntity<>(categoryNameDtoList, HttpStatus.OK);
    }

    @GetMapping("/{categoryName}")
    public ResponseEntity<ResponseCategoryDto> getPostByCategory(@PathVariable String categoryName){

        Category category = categoryService.getCategory(categoryName);
        ResponseCategoryDto responseCategoryDto = new ResponseCategoryDto(category);

        return new ResponseEntity<>(responseCategoryDto, HttpStatus.OK);
    }

}
