package com.biblio.medialltech.books;

import java.util.List;

public interface BookService {

    List<BookDTO> getAllBooks();

    BookDTO getBookById(String id);

    BookDTO createBook(BookDTO bookDTO);

    BookDTO updateBook(String id, BookDTO bookDTO);

    String deleteBook(String id);
}