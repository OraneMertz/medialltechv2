package com.biblio.medialltechv2.service.impl;

import com.biblio.medialltechv2.dto.BooksDTO;
import com.biblio.medialltechv2.model.Books;
import com.biblio.medialltechv2.repository.BooksRepository;
import com.biblio.medialltechv2.service.BooksService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BooksServiceImpl implements BooksService {

    private final BooksRepository booksRepository;

    public BooksServiceImpl(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    public List<BooksDTO> getAllBooks() {
        return booksRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BooksDTO> getBookById(String id) {
        return booksRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public BooksDTO addBook(Books book) {
        Books savedBook = booksRepository.save(book);
        return convertToDTO(savedBook);
    }

    @Override
    public BooksDTO updateBook(String id, Books bookDetails) {
        return booksRepository.findById(id)
                .map(existingBook -> {
                    existingBook.setTitle(bookDetails.getTitle());
                    existingBook.setAuthor(bookDetails.getAuthor());
                    existingBook.setImage(bookDetails.getImage());
                    existingBook.setDisponible(bookDetails.isDisponible());
                    existingBook.setBorrow(bookDetails.getBorrow());
                    existingBook.setBorrowBy(bookDetails.getBorrowBy());
                    Books updatedBook = booksRepository.save(existingBook);

                    return convertToDTO(updatedBook);
                })
                .orElseThrow(() -> new IllegalArgumentException("Le livre avec l'id : " + id + " , n'existe pas."));
    }

    @Override
    public void deleteBook(String id) {
        if (booksRepository.existsById(id)) {
            booksRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Le livre avec l'id : " + id + " , n'existe pas.");
        }
    }

    private BooksDTO convertToDTO(Books book) {
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