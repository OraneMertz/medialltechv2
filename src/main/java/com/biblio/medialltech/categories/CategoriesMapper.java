package com.biblio.medialltech.categories;

import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoriesMapper {

    private final LogService logService;

    public CategoriesMapper(LogService logService) {
        this.logService = logService;
    }

    // Convertir une catégorie en CategoryDTO
    public ServiceResponse<CategoriesDTO> toDTO(Categories categories) {
        if (categories == null) {
            logService.warn("Tentative de mappage d'une catégorie null en DTO.");
            return ServiceResponse.error(ResponseCode.INVALID_DATA, ResponseMessage.CATEGORY_INVALID, null);
        }

        logService.info("Mappage de l'entité Category vers DTO pour la catégorie avec ID: {}", categories.getId());

        CategoriesDTO dto = new CategoriesDTO();
        dto.setId(categories.getId());
        dto.setName(categories.getName());

        return ServiceResponse.success(ResponseCode.SUCCESS, ResponseMessage.CATEGORY_SUCCESS, dto);
    }

    // Convertir un CategoryDTO en catégorie
    public ServiceResponse<Categories> toEntity(CategoriesDTO dto) {
        if (dto == null) {
            logService.warn("Tentative de mappage d'un CategoryDTO null en entité.");
            return ServiceResponse.error(ResponseCode.INVALID_DATA, ResponseMessage.CATEGORY_INVALID, null);
        }

        logService.info("Mappage de CategoryDTO vers l'entité Category pour le DTO avec ID: {}", dto.getId());

        Categories categories = new Categories();
        categories.setId(dto.getId());
        categories.setName(dto.getName());

        return ServiceResponse.success(ResponseCode.SUCCESS, ResponseMessage.CATEGORY_SUCCESS, categories);
    }

    // Convertir une liste de catégories en liste de CategoryDTO
    public ServiceResponse<List<CategoriesDTO>> toDTOList(List<Categories> categories) {
        if (categories == null || categories.isEmpty()) {
            logService.warn("Liste de catégories null ou vide lors du mappage vers une liste de CategoryDTO.");
            return ServiceResponse.error(ResponseCode.INVALID_DATA, ResponseMessage.CATEGORY_NULL, null);
        }

        logService.info("Mappage de la liste des entités Category vers une liste de CategoryDTO.");

        List<CategoriesDTO> categoriesDTOS = categories.stream()
                .map(this::toDTO)
                .map(ServiceResponse::getData)
                .collect(Collectors.toList());

        return ServiceResponse.success(ResponseCode.SUCCESS, ResponseMessage.CATEGORY_SUCCESS, categoriesDTOS);
    }
}

