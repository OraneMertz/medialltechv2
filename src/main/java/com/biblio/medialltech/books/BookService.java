package com.biblio.medialltech.books;

import java.util.List;

public interface BookService {

    List<BookDTO> getAllBooks();

    BookDTO getBookById(Long id);

    BookDTO createBook(BookDTO bookDTO);

    BookDTO updateBook(Long id, BookDTO bookDTO);

    Long deleteBook(Long id);
}
