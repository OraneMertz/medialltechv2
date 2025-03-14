package com.biblio.medialltechv2.service.impl;

import com.biblio.medialltechv2.dto.BooksDTO;
import com.biblio.medialltechv2.dto.BorrowersDTO;
import com.biblio.medialltechv2.model.Books;
import com.biblio.medialltechv2.model.Borrowers;
import com.biblio.medialltechv2.repository.BooksRepository;
import com.biblio.medialltechv2.repository.BorrowersRepository;
import com.biblio.medialltechv2.service.BorrowersService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowersServiceImpl implements BorrowersService {

    private final BorrowersRepository borrowersRepository;
    private final BooksRepository booksRepository;

    public BorrowersServiceImpl(BorrowersRepository borrowersRepository, BooksRepository booksRepository) {
        this.borrowersRepository = borrowersRepository;
        this.booksRepository = booksRepository;
    }

    @Override
    public List<Borrowers> findAll() {
        return borrowersRepository.findAll();
    }

    @Override
    public List<Books> getBorrowedBooks(List<String> borrowedBooksIds) {
        return booksRepository.findAllById(borrowedBooksIds);
    }

    @Override
    public List<BooksDTO> getBorrowedBooksByBorrowerId(String id) {
        Borrowers borrower = borrowersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Emprunteur non trouvé avec l'id : " + id));

        List<Books> borrowedBooks = borrower.getBorrowedBooks();
        return borrowedBooks.stream()
                .map(this::convertToBooksDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BorrowersDTO getBorrowerById(String id) {
        Borrowers borrowers = borrowersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Emprunteur non trouvé avec l'id : " + id));
        return convertBorrowersToDTO(borrowers);
    }

    @Override
    public BorrowersDTO addBorrower(Borrowers borrower) {
        Borrowers savedBorrower = borrowersRepository.save(borrower);
        return convertBorrowersToDTO(savedBorrower);
    }

    @Override
    public BorrowersDTO updateBorrower(String id, Borrowers borrower) {
        Borrowers existingBorrower = borrowersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Emprunteur non trouvé avec l'id : " + id));
        existingBorrower.setUsername(borrower.getUsername());
        existingBorrower.setEmail(borrower.getEmail());
        existingBorrower.setActive(borrower.isActive());

        Borrowers updatedBorrower = borrowersRepository.save(existingBorrower);
        return convertBorrowersToDTO(updatedBorrower);
    }

    @Override
    public void deleteBorrowerById(String id) {
        Borrowers borrower = borrowersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Emprunteur non trouvé avec l'id : " + id));
        borrowersRepository.delete(borrower);
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
}
