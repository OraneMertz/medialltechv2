package com.biblio.medialltech.books;

import com.biblio.medialltech.categories.Category;
import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    private final LogService logService;

    public BookMapper(LogService logService) {
        this.logService = logService;
    }

    public BookDTO toDTO(Book book) {
        if (book == null) {
            logService.warn("Tentative de mapping d'un Book nul vers BookDTO.");
            return null;
        }

        BookDTO dto = new BookDTO();

        // Utilisation de tous les setters
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setImage(book.getImage());
        dto.setStatus(book.getStatus());
        dto.setBorrowerUsername(book.getBorrowerUsername());

        // Conversion des catégories en IDs
        if (book.getCategories() != null && !book.getCategories().isEmpty()) {
            List<String> categoryIds = book.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
            dto.setCategoryIds(categoryIds);
        }

        logService.info("Mappage réussi de Book vers BookDTO avec ID : {}", book.getId());
        return dto;
    }

    public ServiceResponse<Book> toEntity(BookDTO bookDTO) {
        if (bookDTO == null) {
            logService.warn("BookDTO est null.");
            return ServiceResponse.errorNoData(ResponseCode.BAD_REQUEST, ResponseMessage.BOOK_NULL);
        }

        // Validation des données requises
        if (bookDTO.getTitle() == null || bookDTO.getTitle().trim().isEmpty()) {
            logService.warn("BookDTO invalide : titre manquant.");
            return ServiceResponse.errorNoData(ResponseCode.BAD_REQUEST, ResponseMessage.INVALID_BOOK_DETAILS);
        }

        if (bookDTO.getAuthor() == null || bookDTO.getAuthor().trim().isEmpty()) {
            logService.warn("BookDTO invalide : auteur manquant.");
            return ServiceResponse.errorNoData(ResponseCode.BAD_REQUEST, ResponseMessage.INVALID_BOOK_DETAILS);
        }

        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle().trim());
        book.setAuthor(bookDTO.getAuthor().trim());
        book.setImage(bookDTO.getImage());

        // Définir le statut (AVAILABLE par défaut si non spécifié)
        if (bookDTO.getStatus() != null) {
            book.setStatus(bookDTO.getStatus());
        } else {
            book.setStatus(BookStatus.AVAILABLE);
        }

        // Note: Les catégories seront définies dans le service
        // car le mapper ne doit pas accéder aux repositories

        logService.info("Mappage réussi de BookDTO à Book avec titre : {}", bookDTO.getTitle());
        return ServiceResponse.success(ResponseCode.SUCCESS, ResponseMessage.BOOK_SUCCESS, book);
    }

    public void updateEntityFromDTO(Book book, BookDTO dto) {
        if (book == null || dto == null) {
            logService.warn("Impossible de mettre à jour : book ou dto est null");
            return;
        }

        if (dto.getTitle() != null && !dto.getTitle().trim().isEmpty()) {
            book.setTitle(dto.getTitle().trim());
        }

        if (dto.getAuthor() != null && !dto.getAuthor().trim().isEmpty()) {
            book.setAuthor(dto.getAuthor().trim());
        }

        if (dto.getImage() != null) {
            book.setImage(dto.getImage());
        }

        if (dto.getStatus() != null) {
            book.setStatus(dto.getStatus());
        }

        if (dto.getBorrowerUsername() != null) {
            book.setBorrowerUsername(dto.getBorrowerUsername());
        }

        logService.info("Mise à jour de l'entité Book avec ID : {}", book.getId());
    }
}