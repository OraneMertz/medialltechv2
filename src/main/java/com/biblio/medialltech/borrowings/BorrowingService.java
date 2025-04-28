package com.biblio.medialltech.borrowings;

import com.biblio.medialltech.books.BookStatus;
import com.biblio.medialltech.logs.ServiceResponse;

import java.util.List;

public interface BorrowingService {

    // Récupération des livres empruntés
    ServiceResponse<List<BorrowingDTO>> getAllBorrowings();

    // Processus d'emprunt d'un livre
    ServiceResponse<BorrowingDTO> getBorrowingById(Long id);

    // Processus de retour d'un livre
    ServiceResponse<Void> returnBook(Long borrowingId);

    // Récupération d'emprunt par utilisateur
    ServiceResponse<List<BorrowingDTO>> getBorrowingsByUser(String username);

    // Récupération d'emprunt par livres
    ServiceResponse<List<BorrowingDTO>> getBorrowingsByBook(Long bookId);

    // Récupération d'emprunt par catégorie
    ServiceResponse<List<BorrowingDTO>> getBorrowingsByCategoryId(Long categoryId);

    // Récupération d'emprunt par statut
    ServiceResponse<List<BorrowingDTO>> getBorrowingsByStatus(BookStatus status);
}
