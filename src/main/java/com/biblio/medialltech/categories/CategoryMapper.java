package com.biblio.medialltech.categories;

import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    private final LogService logService;

    public CategoryMapper(LogService logService) {
        this.logService = logService;
    }

    public CategoryDTO toDTO(Category category) {
        if (category == null) {
            logService.warn("Tentative de mapping d'une Categories nulle vers CategoriesDTO.");
            return null;
        }

        CategoryDTO dto = new CategoryDTO();
        
        dto.setId(category.getId());
        dto.setName(category.getName());

        logService.info("Mapping réussi pour la catégorie ID : {}", category.getId());
        return dto;
    }

    public ServiceResponse<Category> toEntity(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            logService.warn("CategoriesDTO est null.");
            return ServiceResponse.errorNoData(ResponseCode.BAD_REQUEST, ResponseMessage.CATEGORY_NULL);
        }

        // Validation des données requises
        if (categoryDTO.getName() == null || categoryDTO.getName().trim().isEmpty()) {
            logService.warn("CategoriesDTO invalide : nom manquant.");
            return ServiceResponse.errorNoData(ResponseCode.BAD_REQUEST, ResponseMessage.CATEGORY_INVALID);
        }

        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName().trim());

        // Note: Les livres seront associés dans le service si nécessaire
        // car le mapper ne doit pas accéder aux repositories

        logService.info("Mappage réussi de CategoriesDTO à Categories avec nom : {}", categoryDTO.getName());
        return ServiceResponse.success(ResponseCode.SUCCESS, ResponseMessage.CATEGORY_SUCCESS, category);
    }
}