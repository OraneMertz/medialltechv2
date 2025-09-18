package com.biblio.medialltech.categories;

import java.util.List;


public interface CategoryService {
    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategoryById(Long id);

    CategoryDTO getCategoryByName(String name);

    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    Long deleteCategory(Long id);
}
