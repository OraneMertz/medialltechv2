package com.biblio.medialltech.books;

import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Récupérer tous les livres
    @GetMapping
    public ServiceResponse<List<BookDTO>> getAllBooks() {
        return bookService.getAllBooks();
    }

    // Récupérer un livre par son ID
    @GetMapping("/{id}")
    public ServiceResponse<BookDTO> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    // Récupérer les livres par auteur
    @GetMapping("/author/{author}")
    public ServiceResponse<List<BookDTO>> getBooksByAuthor(@PathVariable String author) {
        return bookService.getBooksByAuthor(author);
    }

    // Récupérer les livres par catégorie
    @GetMapping("/category/{categoryId}")
    public ServiceResponse<List<BookDTO>> getBooksByCategory(@PathVariable Long categoryId) {
        return bookService.getBookByCategory(categoryId);
    }

    // Récupérer les livres d'un emprunteur
    @GetMapping("/borrower/{borrowerUsername}")
    public ServiceResponse<List<BookDTO>> getBooksByBorrower(@PathVariable String borrowerUsername) {
        return bookService.getBooksByBorrower(borrowerUsername);
    }

    // Récupérer les livres disponibles
    @GetMapping("/available")
    public ServiceResponse<List<BookDTO>> getAvailableBooks() {
        return bookService.getAvailableBooks();
    }

    // Créer un nouveau livre
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        return bookService.createBook(bookDTO);
    }

    // Ajouter une image à un livre
    @PostMapping("/{bookId}/image")
    public ServiceResponse<BookDTO> addImageToBook(@PathVariable Long bookId, @RequestParam("file") String imageUrl) {
        return bookService.addImageToBook(bookId, imageUrl);
    }

    // Mettre à jour un livre
    @PutMapping("/{id}")
    public ServiceResponse<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        return bookService.updateBook(id, bookDTO);
    }

    // Supprimer un livre
    @DeleteMapping("/{id}")
    public ServiceResponse<Void> deleteBook(@PathVariable Long id) {
        return bookService.deleteBook(id);
    }
}
