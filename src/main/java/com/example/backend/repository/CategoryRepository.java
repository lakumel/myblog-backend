package com.example.backend.repository;

import com.example.backend.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findCategoryByName(String name);

    @Query("select c from Category c join fetch c.posts p where c.name = :categoryName")
    Optional<Category> findAllWithPost(String categoryName);
}
