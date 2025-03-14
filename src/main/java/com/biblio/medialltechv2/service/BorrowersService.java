package com.biblio.medialltechv2.service;

import com.biblio.medialltechv2.dto.BooksDTO;
import com.biblio.medialltechv2.dto.BorrowersDTO;
import com.biblio.medialltechv2.model.Books;
import com.biblio.medialltechv2.model.Borrowers;

import java.util.List;

public interface BorrowersService {
    List<Borrowers> findAll();

    List<Books> getBorrowedBooks(List<String> borrowedBooksIds);

    BorrowersDTO getBorrowerById(String id);

    BorrowersDTO addBorrower(Borrowers borrower);

    BorrowersDTO updateBorrower(String id, Borrowers borrower);

    void deleteBorrowerById(String id);

    BooksDTO convertToBooksDTO(Books book);

    List<BooksDTO> getBorrowedBooksByBorrowerId(String id);
}

