package com.biblio.medialltech.service;

import com.biblio.medialltech.dto.CategoryDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();

    Optional<CategoryDTO> getCategoryById(Long id);

    Optional<CategoryDTO> getCategoryByName(String name);

    @Transactional
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    @Transactional
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    @Transactional
    boolean deleteCategory(Long id);
}
