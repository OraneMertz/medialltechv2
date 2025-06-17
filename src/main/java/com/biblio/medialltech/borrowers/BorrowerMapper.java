package com.biblio.medialltech.borrowers;

import com.biblio.medialltech.categories.Categories;
import com.biblio.medialltech.categories.CategoriesRepository;
import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BorrowerMapper {

    private final LogService logService;
    private final CategoriesRepository categoriesRepository;

    public BorrowerMapper(LogService logService,
                          CategoriesRepository categoriesRepository) {
        this.logService = logService;
        this.categoriesRepository = categoriesRepository;
    }

    public BorrowerDTO toDTO(Borrower borrower) {
        if (borrower == null) {
            logService.warn("Tentative de mapping d'un Borrower nul vers BorrowerDTO.");
            return null;
        }

        BorrowerDTO dto = new BorrowerDTO();
        
        mapCommonFieldsToDTO(borrower, dto);

        // Conversion des catégories en IDs
        if (borrower.getCategories() != null && !borrower.getCategories().isEmpty()) {
            List<Long> categoryIds = borrower.getCategories().stream()
                    .map(Categories::getId)
                    .toList();
            dto.setCategoryIds(categoryIds);
        }

        logService.info("Mapping réussi pour l'emprunteur ID : {}", borrower.getId());
        return dto;
    }

    public ServiceResponse<Borrower> toEntity(BorrowerDTO borrowerDTO) {
        if (borrowerDTO == null) {
            logService.warn("BorrowerDTO invalide fourni : DTO nul.");
            return ServiceResponse.errorNoData(ResponseCode.BAD_REQUEST, ResponseMessage.BORROWING_DTO_NULL);
        }

        // Validation des champs essentiels
        if (borrowerDTO.getBorrowerUsername() == null || borrowerDTO.getBorrowerUsername().trim().isEmpty()) {
            logService.warn("BorrowerDTO invalide : nom d'utilisateur manquant.");
            return ServiceResponse.errorNoData(ResponseCode.BAD_REQUEST, ResponseMessage.BORROWING_MISSING_IDS);
        }

        if (borrowerDTO.getBookId() == null) {
            logService.warn("BorrowerDTO invalide : bookId manquant.");
            return ServiceResponse.errorNoData(ResponseCode.BAD_REQUEST, ResponseMessage.BORROWING_MISSING_IDS);
        }

        Borrower borrower = new Borrower();

        // Use the helper method for common mapping
        mapCommonFieldsToEntity(borrowerDTO, borrower);

        // Gestion des catégories (optionnel)
        if (borrowerDTO.getCategoryIds() != null && !borrowerDTO.getCategoryIds().isEmpty()) {
            ServiceResponse<List<Categories>> categoriesResult = validateAndGetCategories(borrowerDTO.getCategoryIds());
            if (categoriesResult.isError()) {
                return ServiceResponse.errorNoData(categoriesResult.getStatusCode(), categoriesResult.getMessage());
            }
            borrower.setCategories(categoriesResult.getData());
        }

        logService.info("Mappage réussi de BorrowerDTO à l'entité Borrower : {}", borrowerDTO.getBorrowerUsername());
        return ServiceResponse.success(ResponseCode.SUCCESS, ResponseMessage.BORROWER_SUCCESS, borrower);
    }

    /**
     * Méthode pour mettre à jour un Borrower existant avec les données du DTO
     */
    public void updateEntityFromDTO(Borrower borrower, BorrowerDTO dto) {
        if (borrower == null || dto == null) {
            logService.warn("Impossible de mettre à jour : borrower ou dto est null");
            return;
        }
        
        updateCommonFieldsFromDTO(borrower, dto);

        // Mise à jour des catégories si nécessaire
        if (dto.getCategoryIds() != null) {
            if (dto.getCategoryIds().isEmpty()) {
                borrower.setCategories(new ArrayList<>());
            } else {
                ServiceResponse<List<Categories>> categoriesResult = validateAndGetCategories(dto.getCategoryIds());
                if (!categoriesResult.isError()) {
                    borrower.setCategories(categoriesResult.getData());
                } else {
                    logService.warn("Erreur lors de la mise à jour des catégories pour l'emprunteur ID : {}", borrower.getId());
                }
            }
        }

        logService.info("Mise à jour de l'entité Borrower avec ID : {}", borrower.getId());
    }

    /**
     * Méthode privée pour valider et récupérer les catégories par leurs IDs
     * @param categoryIds Liste des IDs des catégories
     * @return ServiceResponse contenant la liste des catégories ou une erreur
     */
    private ServiceResponse<List<Categories>> validateAndGetCategories(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.BAD_REQUEST,
                    ResponseMessage.INVALID_INPUT,
                    new ArrayList<>(),
                    true,
                    "Liste des IDs de catégories vide ou nulle"
            );
        }

        // Récupérer les catégories par IDs
        List<Categories> categories = categoryIds.stream()
                .map(categoriesRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        // Vérifier que toutes les catégories existent
        if (categories.size() != categoryIds.size()) {
            List<Long> foundIds = categories.stream()
                    .map(Categories::getId)
                    .toList();

            List<Long> missingIds = categoryIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.NOT_FOUND,
                    ResponseMessage.CATEGORY_NOT_FOUND,
                    null,
                    true,
                    "Catégories non trouvées avec les IDs : {}",
                    missingIds
            );
        }

        // Succès : toutes les catégories existent
        return ServiceResponse.logAndRespond(
                logService,
                ResponseCode.SUCCESS,
                ResponseMessage.CATEGORY_SUCCESS,
                categories,
                false,
                "Validation réussie pour {} catégorie(s)",
                categories.size()
        );
    }

    /**
     * Méthode pour faire correspondre les champs communs de Borrower à BorrowerDTO.
     */
    private void mapCommonFieldsToDTO(Borrower borrower, BorrowerDTO dto) {
        dto.setId(borrower.getId());
        dto.setBookId(borrower.getBookId());
        dto.setTitle(borrower.getTitle());
        dto.setAuthor(borrower.getAuthor());
        dto.setImage(borrower.getImage());
        dto.setBorrowerUsername(borrower.getBorrowerUsername());
        dto.setBorrowDate(borrower.getBorrowDate());
        dto.setStatus(borrower.getStatus());
    }

    /**
     * Méthode pour mapper les champs communs de BorrowerDTO à Borrower.
     */
    private void mapCommonFieldsToEntity(BorrowerDTO borrowerDTO, Borrower borrower) {
        borrower.setId(borrowerDTO.getId());
        borrower.setBookId(borrowerDTO.getBookId());
        borrower.setTitle(borrowerDTO.getTitle());
        borrower.setAuthor(borrowerDTO.getAuthor());
        borrower.setImage(borrowerDTO.getImage());
        borrower.setBorrowerUsername(borrowerDTO.getBorrowerUsername());
        borrower.setBorrowDate(borrowerDTO.getBorrowDate());
        borrower.setStatus(borrowerDTO.getStatus());
    }

    /**
     * Méthode pour mettre à jour les champs communs d'un emprunteur existant à partir d'un DTO d'emprunteur.
     * Les champs ne sont mis à jour que s'ils ne sont pas nulls dans le DTO.
     */
    private void updateCommonFieldsFromDTO(Borrower borrower, BorrowerDTO dto) {
        if (dto.getId() != null) {
            borrower.setId(dto.getId());
        }
        if (dto.getBookId() != null) {
            borrower.setBookId(dto.getBookId());
        }
        if (dto.getTitle() != null) {
            borrower.setTitle(dto.getTitle());
        }
        if (dto.getAuthor() != null) {
            borrower.setAuthor(dto.getAuthor());
        }
        if (dto.getImage() != null) {
            borrower.setImage(dto.getImage());
        }
        if (dto.getBorrowerUsername() != null) {
            borrower.setBorrowerUsername(dto.getBorrowerUsername());
        }
        if (dto.getBorrowDate() != null) {
            borrower.setBorrowDate(dto.getBorrowDate());
        }
        if (dto.getStatus() != null) {
            borrower.setStatus(dto.getStatus());
        }
    }
}