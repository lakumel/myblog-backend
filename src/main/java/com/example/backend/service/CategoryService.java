package com.example.backend.service;

import com.example.backend.domain.Category;
import com.example.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category saveOrFindCategory(String categoryName) {
        Optional<Category> optional = categoryRepository.findCategoryByName(categoryName);
//        Optional<Category> optional = categoryRepository.findAllWithPost(categoryName);

        if (optional.isEmpty()) {
            Category category = Category.builder()
                    .name(categoryName)
                    .build();
            return categoryRepository.save(category);
        } else {
            return optional.get();
        }
    }

    public Category getCategory(String categoryName){
        Optional<Category> optional = categoryRepository.findAllWithPost(categoryName);

        return optional.get();
//        if(optional.isEmpty()){
//        }
    }

    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }
}
