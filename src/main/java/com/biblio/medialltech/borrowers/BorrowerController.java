package com.biblio.medialltech.borrowers;

import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {

    private final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    // Récupérer tous les emprunteurs
    @GetMapping
    public ServiceResponse<List<BorrowerDTO>> getAllBorrowers() {
        return borrowerService.getAllBorrowers();
    }

    // Récupérer un emprunteur par ID
    @GetMapping("/{id}")
    public ServiceResponse<BorrowerDTO> getBorrowerById(@PathVariable Long id) {
        return borrowerService.getBorrowerById(id);
    }

    // Rechercher des emprunteurs par nom d'utilisateur
    @GetMapping("/username/{username}")
    public ServiceResponse<List<BorrowerDTO>> getBorrowersByUsername(@PathVariable String username) {
        return borrowerService.getBorrowersByUsername(username);
    }

    // Récupérer les emprunteurs d'un livre spécifique
    @GetMapping("/book/{bookId}")
    public ServiceResponse<List<BorrowerDTO>> getBorrowersByBookId(@PathVariable Long bookId) {
        return borrowerService.getBorrowersByBookId(bookId);
    }

    // Récupérer les emprunteurs par catégorie
    @GetMapping("/category/{categoryId}")
    public ServiceResponse<List<BorrowerDTO>> getBorrowersByCategory(@PathVariable Long categoryId) {
        return borrowerService.getBorrowersByCategory(categoryId);
    }

    // Créer un nouvel emprunteur
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse<BorrowerDTO> createBorrower(@RequestBody BorrowerDTO borrowerDTO) {
        return borrowerService.createBorrower(borrowerDTO);
    }

    // Mettre à jour un emprunteur
    @PutMapping("/{id}")
    public ServiceResponse<BorrowerDTO> updateBorrower(@PathVariable Long id, @RequestBody BorrowerDTO borrowerDTO) {
        return borrowerService.updateBorrower(id, borrowerDTO);
    }

    // Supprimer un emprunteur
    @DeleteMapping("/{id}")
    public ServiceResponse<Void> deleteBorrower(@PathVariable Long id) {
        return borrowerService.deleteBorrower(id);
    }
}