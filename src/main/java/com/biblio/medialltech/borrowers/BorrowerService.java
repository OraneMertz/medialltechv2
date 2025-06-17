package com.biblio.medialltech.borrowers;

import com.biblio.medialltech.books.BookStatus;
import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BorrowerService {

    ServiceResponse<List<BorrowerDTO>> getAllBorrowers();

    ServiceResponse<BorrowerDTO> getBorrowerById(Long id);

    ServiceResponse<List<BorrowerDTO>> getBorrowersByUsername(String username);

    ServiceResponse<List<BorrowerDTO>> getBorrowersByBookId(Long bookId);

    ServiceResponse<List<BorrowerDTO>> getBorrowersByCategory(Long categoryId);
    
    ServiceResponse<BorrowerDTO> createBorrower(BorrowerDTO borrowerDTO);
    
    ServiceResponse<BorrowerDTO> updateBorrower(Long id, BorrowerDTO borrowerDTO);

    ServiceResponse<Void> deleteBorrower(Long id);

    ServiceResponse<List<BorrowerDTO>> getBorrowersByStatus(BookStatus status);

    ServiceResponse<BorrowerDTO> getBorrowerByExactUsername(String username);
}
