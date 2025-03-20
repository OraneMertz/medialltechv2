package com.biblio.medialltech.mapper;

import com.biblio.medialltech.dto.BookDTO;
import com.biblio.medialltech.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookDTO toDTO(Book book) {
        if (book == null) {
            return null;
        }
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getImage(),
                book.isDisponible(),
                book.getBorrower(),
                book.getCategory()
        );
    }

    public Book toEntity(BookDTO bookDTO) {
        if (bookDTO == null) {
            return null;
        }
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setImage(bookDTO.getImage());
        book.setDisponible(bookDTO.isDisponible());
        book.setBorrower(bookDTO.getBorrower());
        book.setCategory(bookDTO.getCategory());
        return book;
    }
}