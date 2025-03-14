package com.biblio.medialltechv2.controller;

import com.biblio.medialltechv2.dto.BooksDTO;
import com.biblio.medialltechv2.dto.BorrowersDTO;
import com.biblio.medialltechv2.model.Books;
import com.biblio.medialltechv2.model.Borrowers;
import com.biblio.medialltechv2.service.BorrowersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/emprunteurs")
public class BorrowersController {

    private final BorrowersService borrowersService;

    public BorrowersController(BorrowersService borrowersService) {
        this.borrowersService = borrowersService;
    }

    @GetMapping
    public List<BorrowersDTO> getAllBorrowers() {
        List<Borrowers> borrowersList = borrowersService.findAll();
        return borrowersList.stream()
                .map(this::convertBorrowersToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BorrowersDTO getBorrowerById(@PathVariable String id) {
        return borrowersService.getBorrowerById(id);
    }

    @GetMapping("/{id}/borrowedBooks")
    public ResponseEntity<List<BooksDTO>> getBorrowedBooks(@PathVariable String id) {
        try {
            List<BooksDTO> booksDTOs = borrowersService.getBorrowedBooksByBorrowerId(id);
            return new ResponseEntity<>(booksDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public BorrowersDTO addBorrower(@RequestBody Borrowers borrower) {
        return borrowersService.addBorrower(borrower);
    }

    @PutMapping("/{id}")
    public BorrowersDTO updateBorrower(@PathVariable String id, @RequestBody Borrowers borrower) {
        return borrowersService.updateBorrower(id, borrower);
    }

    @DeleteMapping("/{id}")
    public void deleteBorrower(@PathVariable String id) {
        borrowersService.deleteBorrowerById(id);
    }

    private BorrowersDTO convertBorrowersToDTO(Borrowers borrower) {
        return new BorrowersDTO(
                borrower.getId(),
                borrower.getUsername(),
                borrower.getEmail(),
                borrower.isActive(),
                borrower.getBorrowedBooks().stream()
                        .map(this::convertToBooksDTO)
                        .toList()
        );
    }

    public BooksDTO convertToBooksDTO(Books book) {
        return new BooksDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getImage(),
                book.isDisponible(),
                book.getBorrow(),
                book.getBorrowBy() != null ? Integer.parseInt(book.getBorrowBy().getId()) : null
        );
    }
}
