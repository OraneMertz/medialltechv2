package com.biblio.medialltech.categories;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategoryById(String id);

    CategoryDTO getCategoryByName(String name);

    CategoryDTO updateCategory(String id, CategoryDTO categoryDTO);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    String deleteCategory(String id);
}