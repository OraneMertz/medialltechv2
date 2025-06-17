package com.biblio.medialltech.borrowings;

import com.biblio.medialltech.books.BookStatus;
import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {

    private final BorrowingService borrowingService;

    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    // Récupérer tous les emprunts
    @GetMapping
    public ServiceResponse<List<BorrowingDTO>> getAllBorrowings() {
        return borrowingService.getAllBorrowings();
    }

    // Récupérer un emprunt par ID
    @GetMapping("/{id}")
    public ServiceResponse<BorrowingDTO> getBorrowingById(@PathVariable Long id) {
        return borrowingService.getBorrowingById(id);
    }

    // Récupérer les emprunts par utilisateur
    @GetMapping("/user/{username}")
    public ServiceResponse<List<BorrowingDTO>> getBorrowingsByUser(@PathVariable String username) {
        return borrowingService.getBorrowingsByUser(username);
    }

    // Récupérer les emprunts par livre
    @GetMapping("/book/{bookId}")
    public ServiceResponse<List<BorrowingDTO>> getBorrowingsByBook(@PathVariable Long bookId) {
        return borrowingService.getBorrowingsByBook(bookId);
    }

    // Récupérer les emprunts par catégorie
    @GetMapping("/category/{categoryId}")
    public ServiceResponse<List<BorrowingDTO>> getBorrowingsByCategoryId(@PathVariable Long categoryId) {
        return borrowingService.getBorrowingsByCategoryId(categoryId);
    }

    // Récupérer les emprunts par statut
    @GetMapping("/status/{status}")
    public ServiceResponse<List<BorrowingDTO>> getBorrowingsByStatus(@PathVariable BookStatus status) {
        return borrowingService.getBorrowingsByStatus(status);
    }

    // Créer un nouvel emprunt
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse<BorrowingDTO> createBorrowing(@RequestBody BorrowingDTO borrowingDTO) {
        return borrowingService.createBorrowing(borrowingDTO);
    }

    // Retourner un livre (supprimer l'emprunt)
    @DeleteMapping("/{borrowingId}/return")
    public ServiceResponse<Void> returnBook(@PathVariable Long borrowingId) {
        return borrowingService.returnBook(borrowingId);
    }
}