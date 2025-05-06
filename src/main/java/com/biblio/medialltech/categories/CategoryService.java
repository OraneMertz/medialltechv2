package com.biblio.medialltech.categories;

import com.biblio.medialltech.logs.ServiceResponse;

import java.util.List;


public interface CategoryService {

    ServiceResponse<List<CategoryDTO>> getAllCategories();

    ServiceResponse<CategoryDTO> getCategoryById(Long id);

    ServiceResponse<CategoryDTO> getCategoryByName(String name);
    
    ServiceResponse<CategoryDTO> updateCategory(Long id, CategoryDTO categoryDTO);

    ServiceResponse<CategoryDTO> createCategory(CategoryDTO categoryDTO);

    ServiceResponse<Void> deleteCategory(Long id);
}
