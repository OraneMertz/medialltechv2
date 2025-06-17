package com.biblio.medialltech.borrowings;

import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.stereotype.Component;

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

        logService.info("Mappage réussi de Borrowing vers BorrowingDTO avec ID : {}", borrowing.getId());

        BorrowingDTO dto = new BorrowingDTO();
        dto.setId(borrowing.getId());
        dto.setBookId(borrowing.getBook() != null ? borrowing.getBook().getId() : null);
        dto.setUsername(borrowing.getUser() != null ? borrowing.getUser().getUsername() : null);
        dto.setBorrowDate(borrowing.getBorrowDate());
        dto.setReturnDate(borrowing.getReturnDate());

        return dto;
    }

    public ServiceResponse<Borrowing> toEntity(BorrowingDTO borrowingDTO) {
        if (borrowingDTO == null) {
            logService.warn("BorrowingDTO est null.");
            return ServiceResponse.errorNoData(ResponseCode.BAD_REQUEST, ResponseMessage.BORROWING_DTO_NULL);
        }

        // Validation des données requises
        if (borrowingDTO.getUsername() == null || borrowingDTO.getUsername().trim().isEmpty()) {
            logService.warn("BorrowingDTO invalide : username manquant.");
            return ServiceResponse.errorNoData(ResponseCode.BAD_REQUEST, ResponseMessage.BORROWING_MISSING_IDS);
        }

        if (borrowingDTO.getBookId() == null) {
            logService.warn("BorrowingDTO invalide : bookId manquant.");
            return ServiceResponse.errorNoData(ResponseCode.BAD_REQUEST, ResponseMessage.BORROWING_MISSING_IDS);
        }

        Borrowing borrowing = new Borrowing();
        borrowing.setId(borrowingDTO.getId());
        borrowing.setBorrowDate(borrowingDTO.getBorrowDate());
        borrowing.setReturnDate(borrowingDTO.getReturnDate());

        // Note: Book et User seront définis dans le service
        // car le mapper ne doit pas accéder aux repositories

        logService.info("Mappage réussi de BorrowingDTO à Borrowing avec username : {}", borrowingDTO.getUsername());
        return ServiceResponse.success(ResponseCode.SUCCESS, ResponseMessage.BORROWING_SUCCESS, borrowing);
    }
}