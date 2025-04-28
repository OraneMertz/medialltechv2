package com.biblio.medialltech.borrowings;

import com.biblio.medialltech.logs.LogService;
import org.springframework.stereotype.Component;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;

/**
 * Mapper entre BorrowingDTO et Borrowing.
 * Note : l'association aux entités Book et User doit être réalisée dans le service.
 */
@Component
public class BorrowingMapper {

    private final LogService logService;

    public BorrowingMapper(LogService logService) {
        this.logService = logService;
    }

    public BorrowingDTO toDTO(Borrowing borrowing) {
        if (borrowing == null) {
            logService.warn("Tentative de mapping d'un Borrowing nul vers BorrowingDTO.");
            return null;
        }

        // Log de succès
        logService.info("Mappage réussi de Borrowing vers BorrowingDTO avec ID : {}", borrowing.getId());

        return new BorrowingDTO(
                borrowing.getId(),
                borrowing.getBook() != null ? borrowing.getBook().getId() : null,
                borrowing.getUser() != null ? borrowing.getUser().getId() : null,
                borrowing.getBorrowDate(),
                borrowing.getReturnDate()
        );
    }

    public ServiceResponse<Borrowing> toEntity(BorrowingDTO borrowingDTO) {
        if (borrowingDTO == null) {
            logService.warn("BorrowingDTO est null.");
            return ServiceResponse.errorNoData(ResponseCode.NOT_FOUND, ResponseMessage.BORROWING_NOT_FOUND);
        }

        // Validation des IDs
        if (borrowingDTO.getUserId() == null || borrowingDTO.getBookId() == null) {
            logService.warn("BorrowingDTO invalide : bookId ou userId manquant.");
            return ServiceResponse.errorNoData(ResponseCode.NOT_FOUND, ResponseMessage.BORROWING_MISSING_IDS);
        }

        Borrowing borrowing = new Borrowing();
        borrowing.setId(borrowingDTO.getId());
        borrowing.setBorrowDate(borrowingDTO.getBorrowDate());
        borrowing.setReturnDate(borrowingDTO.getReturnDate());

        // Log de succès
        logService.info("Mappage réussi de BorrowingDTO à Borrowing avec ID : {}", borrowingDTO.getId());
        return ServiceResponse.success(ResponseCode.SUCCESS, ResponseMessage.BORROWING_SUCCESS, borrowing);
    }
}


