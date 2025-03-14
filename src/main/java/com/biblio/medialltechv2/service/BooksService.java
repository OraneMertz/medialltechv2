package com.biblio.medialltechv2.service;

import com.biblio.medialltechv2.dto.BooksDTO;
import com.biblio.medialltechv2.model.Books;

import java.util.List;
import java.util.Optional;

public interface BooksService {
    List<BooksDTO> getAllBooks();

    Optional<BooksDTO> getBookById(String id);

    BooksDTO addBook(Books book);

    BooksDTO updateBook(String id, Books bookDetails);

    void deleteBook(String id);
}

