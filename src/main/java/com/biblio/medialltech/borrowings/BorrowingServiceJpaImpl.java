package com.biblio.medialltech.borrowings;

import com.biblio.medialltech.books.Book;
import com.biblio.medialltech.books.BookStatus;
import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import com.biblio.medialltech.books.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BorrowingServiceJpaImpl implements BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;
    private final BorrowingMapper borrowingMapper;
    private final LogService logService;

    public BorrowingServiceJpaImpl(BorrowingRepository borrowingRepository, 
                                   BookRepository bookRepository,
                                   BorrowingMapper borrowingMapper,
                                   LogService logService) {
        this.borrowingRepository = borrowingRepository;
        this.bookRepository = bookRepository;
        this.borrowingMapper = borrowingMapper;
        this.logService = logService;
    }

    // Récupération des livres empruntés
    @Override
    public ServiceResponse<List<BorrowingDTO>> getAllBorrowings() {
        try {
            List<BorrowingDTO> borrowings = borrowingRepository.findAll().stream()
                    .map(borrowingMapper::toDTO)
                    .collect(Collectors.toList());

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BORROWING_SUCCESS,
                    borrowings,
                    false,
                    "Récupération de {} emprunts avec succès",
                    borrowings.size()
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la récupération des emprunts");
        }
    }

    // Processus d'emprunt d'un livre
    @Transactional
    @Override
    public ServiceResponse<BorrowingDTO> getBorrowingById(Long id) {
        try {
            Optional<Borrowing> borrowingOpt = borrowingRepository.findById(id);

            if (borrowingOpt.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.BORROWING_NOT_FOUND,
                        null,
                        false,
                        "L'emprunt avec l'ID '{}' n'a pas été trouvé",
                        id
                );
            }

            BorrowingDTO borrowingDTO = borrowingMapper.toDTO(borrowingOpt.get());
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BORROWING_SUCCESS,
                    borrowingDTO,
                    false,
                    "Emprunt récupéré avec succès: {}",
                    borrowingDTO.getId()
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la récupération de l'emprunt avec l'ID: {}", id);
        }
    }

    // Récupération d'emprunt par utilisateur
    @Override
    public ServiceResponse<List<BorrowingDTO>> getBorrowingsByUser(String username) {
        try {
            List<Borrowing> borrowings = borrowingRepository.findByUserUsername(username);

            if (borrowings.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.BORROWING_NOT_FOUND,
                        null,
                        false,
                        "Aucun emprunt trouvé pour l'utilisateur: '{}'",
                        username
                );
            }

            List<BorrowingDTO> borrowingDTOs = borrowings.stream()
                    .map(borrowingMapper::toDTO)
                    .collect(Collectors.toList());

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BORROWING_SUCCESS,
                    borrowingDTOs,
                    false,
                    "{} emprunt(s) trouvé(s) pour l'utilisateur '{}'",
                    borrowingDTOs.size(),
                    username
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la récupération des emprunts pour l'utilisateur '{}'", username);
        }
    }

    // Récupération d'emprunt par livres
    @Override
    public ServiceResponse<List<BorrowingDTO>> getBorrowingsByBook(Long bookId) {
        try {
            List<Borrowing> borrowings = borrowingRepository.findByBookId(bookId);

            if (borrowings.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.BORROWING_NOT_FOUND,
                        null,
                        false,
                        "Aucun emprunt trouvé pour le livre avec l'ID: '{}'",
                        bookId
                );
            }

            List<BorrowingDTO> borrowingDTOs = borrowings.stream()
                    .map(borrowingMapper::toDTO)
                    .collect(Collectors.toList());

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BORROWING_SUCCESS,
                    borrowingDTOs,
                    false,
                    "{} emprunt(s) trouvé(s) pour le livre avec l'ID '{}'",
                    borrowingDTOs.size(),
                    bookId
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la récupération des emprunts pour le livre avec l'ID: '{}'", bookId);
        }
    }

    // Processus de retour d'un livre
    @Transactional
    @Override
    public ServiceResponse<Void> returnBook(Long borrowingId) {
        try {
            Optional<Borrowing> borrowingOpt = borrowingRepository.findById(borrowingId);

            if (borrowingOpt.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.BORROWING_NOT_FOUND,
                        null,
                        false,
                        "Aucun emprunt trouvé pour l'ID '{}' ou le livre n'a pas été emprunté",
                        borrowingId
                );
            }

            Borrowing borrowing = borrowingOpt.get();
            Book borrowedBook = borrowing.getBook();

            if (borrowedBook.getStatus() != BookStatus.BORROWED) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.BOOK_ALREADY_RETURNED,
                        null,
                        false,
                        "Le livre '{}' a déjà été retourné",
                        borrowedBook.getTitle()
                );
            }

            borrowedBook.setStatus(BookStatus.AVAILABLE);
            borrowedBook.setBorrowerUsername(null);
            bookRepository.save(borrowedBook);
            borrowingRepository.delete(borrowing);

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BORROWING_RETURNED,
                    null,
                    false,
                    "Le livre '{}' a été retourné avec succès",
                    borrowedBook.getTitle()
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors du retour du livre avec l'ID d'emprunt '{}'", borrowingId);
        }
    }

    // Récupération d'emprunt par catégorie
    @Override
    public ServiceResponse<List<BorrowingDTO>> getBorrowingsByCategoryId(Long categoryId) {
        try {
            List<Book> books = bookRepository.findByCategoriesId(categoryId);

            if (books.isEmpty()) {
                logService.info("Aucun livre trouvé pour la catégorie avec l'ID '{}'.", categoryId);
                return ServiceResponse.success(
                        ResponseCode.SUCCESS,
                        ResponseMessage.BOOK_NOT_FOUND,
                        List.of()
                );
            }

            List<Borrowing> borrowings = borrowingRepository.findByBookIn(books);

            if (borrowings.isEmpty()) {
                logService.info("Aucun emprunt trouvé pour les livres de la catégorie avec l'ID '{}'.", categoryId);
                return ServiceResponse.success(
                        ResponseCode.SUCCESS,
                        ResponseMessage.NO_BORROWINGS,
                        List.of()
                );
            }

            List<BorrowingDTO> borrowingDTOs = borrowings.stream()
                    .map(borrowingMapper::toDTO)
                    .collect(Collectors.toList());

            logService.info("Emprunts récupérés pour la catégorie avec l'ID '{}'.", categoryId);
            return ServiceResponse.success(
                    ResponseCode.SUCCESS,
                    ResponseMessage.BORROWING_SUCCESS,
                    borrowingDTOs
            );
        } catch (Exception e) {
            logService.error("Erreur lors de la récupération des emprunts pour la catégorie avec l'ID '{}'.", categoryId, e);
            return ServiceResponse.handleException(
                    logService,
                    e,
                    "Erreur lors de la récupération des emprunts pour la catégorie avec l'ID '{}'",
                    categoryId
            );
        }
    }

    // Récupération d'emprunt par statut
    @Override
    public ServiceResponse<List<BorrowingDTO>> getBorrowingsByStatus(BookStatus status) {
        try {
            List<Book> books = bookRepository.findByStatus(status);

            if (books.isEmpty()) {
                logService.info("Aucun livre trouvé avec le statut '{}'.", status);
                return ServiceResponse.success(
                        ResponseCode.SUCCESS,
                        ResponseMessage.BOOK_NOT_FOUND,
                        List.of()
                );
            }

            List<Borrowing> borrowings = borrowingRepository.findByBookIn(books);

            if (borrowings.isEmpty()) {
                logService.info("Aucun emprunt trouvé pour les livres avec le statut '{}'.", status);
                return ServiceResponse.success(
                        ResponseCode.SUCCESS,
                        ResponseMessage.NO_BORROWINGS,
                        List.of()
                );
            }

            List<BorrowingDTO> borrowingDTOs = borrowings.stream()
                    .map(borrowingMapper::toDTO)
                    .collect(Collectors.toList());
            
            logService.info("Emprunts récupérés pour les livres avec le statut '{}'.", status);
            return ServiceResponse.success(
                    ResponseCode.SUCCESS,
                    ResponseMessage.BORROWING_SUCCESS,
                    borrowingDTOs
            );
        } catch (Exception e) {
            logService.error("Erreur lors de la récupération des emprunts pour les livres avec le statut '{}'.", status, e);
            return ServiceResponse.handleException(
                    logService,
                    e,
                    "Erreur lors de la récupération des emprunts pour les livres avec le statut '{}'",
                    status
            );
        }
    }
}