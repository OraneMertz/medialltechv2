package com.biblio.medialltech.books;

import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {

    ServiceResponse<List<BookDTO>> getAllBooks();

    ServiceResponse<BookDTO> getBookById(Long id);

    ServiceResponse<List<BookDTO>> getBooksByAuthor(String author);

    ServiceResponse<List<BookDTO>> getBookByCategory(Long categoryId);

    ServiceResponse<List<BookDTO>> getBooksByBorrower(String borrowerUsername);

    ServiceResponse<List<BookDTO>> getAvailableBooks();
    
    ServiceResponse<BookDTO> createBook(BookDTO bookDTO);

    ServiceResponse<BookDTO> addImageToBook(Long bookId, MultipartFile file);

    ServiceResponse<BookDTO> updateBook(Long id, BookDTO bookDTO);

    ServiceResponse<Void> deleteBook(Long id);
}
