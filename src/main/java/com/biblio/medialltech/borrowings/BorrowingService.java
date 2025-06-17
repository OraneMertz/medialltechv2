package com.biblio.medialltech.borrowings;

import com.biblio.medialltech.books.BookStatus;
import com.biblio.medialltech.logs.ServiceResponse;

import java.util.List;

public interface BorrowingService {

    ServiceResponse<List<BorrowingDTO>> getAllBorrowings();

    ServiceResponse<BorrowingDTO> getBorrowingById(Long id);

    ServiceResponse<Void> returnBook(Long borrowingId);

    ServiceResponse<List<BorrowingDTO>> getBorrowingsByUser(String username);

    ServiceResponse<List<BorrowingDTO>> getBorrowingsByBook(Long bookId);

    ServiceResponse<List<BorrowingDTO>> getBorrowingsByCategoryId(Long categoryId);

    ServiceResponse<List<BorrowingDTO>> getBorrowingsByStatus(BookStatus status);
    
    ServiceResponse<BorrowingDTO> createBorrowing(BorrowingDTO borrowingDTO);
}
