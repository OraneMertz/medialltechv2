package com.biblio.medialltech.categories;

import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoriesMapper {

    private final LogService logService;

    public CategoriesMapper(LogService logService) {
        this.logService = logService;
    }

    public CategoriesDTO toDTO(Categories category) {
        if (category == null) {
            logService.warn("Tentative de mapping d'une Categories nulle vers CategoriesDTO.");
            return null;
        }

        CategoriesDTO dto = new CategoriesDTO();
        
        dto.setId(category.getId());
        dto.setName(category.getName());

        logService.info("Mapping réussi pour la catégorie ID : {}", category.getId());
        return dto;
    }

    public ServiceResponse<Categories> toEntity(CategoriesDTO categoryDTO) {
        if (categoryDTO == null) {
            logService.warn("CategoriesDTO est null.");
            return ServiceResponse.errorNoData(ResponseCode.BAD_REQUEST, ResponseMessage.CATEGORY_NULL);
        }

        // Validation des données requises
        if (categoryDTO.getName() == null || categoryDTO.getName().trim().isEmpty()) {
            logService.warn("CategoriesDTO invalide : nom manquant.");
            return ServiceResponse.errorNoData(ResponseCode.BAD_REQUEST, ResponseMessage.CATEGORY_INVALID);
        }

        Categories category = new Categories();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName().trim());

        // Note: Les livres seront associés dans le service si nécessaire
        // car le mapper ne doit pas accéder aux repositories

        logService.info("Mappage réussi de CategoriesDTO à Categories avec nom : {}", categoryDTO.getName());
        return ServiceResponse.success(ResponseCode.SUCCESS, ResponseMessage.CATEGORY_SUCCESS, category);
    }

    /**
     * Convertir une liste de Categories en liste de CategoriesDTO
     */
    public List<CategoriesDTO> toDTOList(List<Categories> categories) {
        if (categories == null) {
            return null;
        }

        return categories.stream()
                .map(this::toDTO)
                .toList();
    }
}