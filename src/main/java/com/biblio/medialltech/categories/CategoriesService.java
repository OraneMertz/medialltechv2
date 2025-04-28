package com.biblio.medialltech.categories;

import com.biblio.medialltech.logs.ServiceResponse;

import java.util.List;


public interface CategoriesService {

    ServiceResponse<List<CategoriesDTO>> getAllCategories();

    ServiceResponse<CategoriesDTO> getCategoryById(Long id);

    ServiceResponse<CategoriesDTO> getCategoryByName(String name);
    
    ServiceResponse<CategoriesDTO> updateCategory(Long id, CategoriesDTO categoriesDTO);

    ServiceResponse<CategoriesDTO> createCategory(CategoriesDTO categoriesDTO);

    ServiceResponse<Void> deleteCategory(Long id);
}
