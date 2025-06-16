package com.biblio.medialltech.borrowers;

import com.biblio.medialltech.books.BookStatus;
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
import java.util.stream.Collectors;

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

        List<Long> categoryIds = borrower.getCategories() != null ? borrower.getCategories().stream()
                .map(Categories::getId)
                .collect(Collectors.toList()) : new ArrayList<>();

        String statusString = borrower.getStatus() != null ? borrower.getStatus().name() : null;

        logService.info("Mapping réussi pour l'emprunteur : {}", borrower.getBorrowerUsername());

        return new BorrowerDTO(
                borrower.getBookId(),
                borrower.getTitle(),
                borrower.getAuthor(),
                borrower.getImage(),
                borrower.getBorrowerUsername(),
                borrower.getBorrowDate(),
                categoryIds,
                statusString
        );
    }

    public ServiceResponse<Borrower> toEntity(BorrowerDTO borrowerDTO) {
        if (borrowerDTO == null) {
            logService.warn("BorrowerDTO invalide fourni : DTO nul.");
            return ServiceResponse.errorNoData(ResponseCode.NO_CONTENT, ResponseMessage.BORROWING_DTO_NULL);
        }

        if (borrowerDTO.getCategories() == null || borrowerDTO.getCategories().isEmpty()) {
            logService.warn("BorrowerDTO invalide fourni : catégorie nulle ou manquante.");
            return ServiceResponse.errorNoData(ResponseCode.NO_CONTENT, ResponseMessage.CATEGORY_NULL);
        }

        Borrower borrower = new Borrower();
        borrower.setBookId(borrowerDTO.getBookId());
        borrower.setTitle(borrowerDTO.getTitle());
        borrower.setAuthor(borrowerDTO.getAuthor());
        borrower.setImage(borrowerDTO.getImage());
        borrower.setBorrowerUsername(borrowerDTO.getBorrowerUsername());
        borrower.setBorrowDate(borrowerDTO.getBorrowDate());

        // Récupérer les entités de catégorie à partir des IDs
        List<Categories> categories = borrowerDTO.getCategories().stream()
                .map(categoriesRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        borrower.setCategories(categories);

        // Convertir le String en enum
        if (borrowerDTO.getStatus() != null) {
            try {
                BookStatus status = BookStatus.valueOf(borrowerDTO.getStatus());
                borrower.setStatus(status);
            } catch (IllegalArgumentException e) {
                logService.warn("Status invalide fourni : {}", borrowerDTO.getStatus());
                return ServiceResponse.errorNoData(ResponseCode.BAD_REQUEST, ResponseMessage.INVALID_STATUS);
            }
        }

        logService.info("Mappage réussi de BorrowerDTO à l'entité Borrower : {}", borrowerDTO.getBorrowerUsername());

        return ServiceResponse.success(ResponseCode.SUCCESS, ResponseMessage.BORROWER_SUCCESS, borrower);
    }
}