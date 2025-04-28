package com.biblio.medialltech.books;

import com.biblio.medialltech.categories.Categories;
import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import com.biblio.medialltech.categories.CategoriesRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    private final LogService logService;
    private final CategoriesRepository categoriesRepository;

    public BookMapper(LogService logService,
                      CategoriesRepository categoriesRepository) {
        this.logService = logService;
        this.categoriesRepository = categoriesRepository;
    }

    public BookDTO toDTO(Book book) {
        if (book == null) {
            logService.warn("Tentative de mapping d'un Book nul vers BookDTO.");
            return null;
        }

        // Convertir les IDs en List<Long>
        List<Long> categoryIds = book.getCategories() != null ? book.getCategories().stream()
                .map(Categories::getId)
                .collect(Collectors.toList()) : new ArrayList<>();

        logService.info("Mapping réussi pour le livre : {}", book.getTitle());

        return new BookDTO(
                book.getTitle(),
                book.getAuthor(),
                book.getImage(),
                categoryIds
        );
    }

    public ServiceResponse<Book> toEntity(BookDTO bookDTO) {
        if (bookDTO == null || bookDTO.getCategory() == null || bookDTO.getCategory().isEmpty()) {
            logService.warn("BookDTO invalide fourni : catégorie nulle ou manquante.");
            return ServiceResponse.errorNoData(ResponseCode.NO_CONTENT, ResponseMessage.CATEGORY_NULL);
        }

        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setImage(bookDTO.getImage());

        // Récupérer les entités de catégorie à partir des IDs.
        List<Categories> categories = bookDTO.getCategory().stream()
                .map(categoriesRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        book.setCategories(categories);

        logService.info("Mappage réussi de BookDTO à l'entité Book : {}", bookDTO.getTitle());

        return ServiceResponse.success(ResponseCode.SUCCESS, ResponseMessage.BOOK_SUCCESS, book);
    }
}
