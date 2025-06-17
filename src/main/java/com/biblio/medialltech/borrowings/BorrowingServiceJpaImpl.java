package com.biblio.medialltech.borrowings;

import com.biblio.medialltech.books.Book;
import com.biblio.medialltech.books.BookStatus;
import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import com.biblio.medialltech.books.BookRepository;
import com.biblio.medialltech.users.User;
import com.biblio.medialltech.users.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BorrowingServiceJpaImpl implements BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowingMapper borrowingMapper;
    private final LogService logService;

    public BorrowingServiceJpaImpl(BorrowingRepository borrowingRepository,
                                   BookRepository bookRepository,
                                   UserRepository userRepository,
                                   BorrowingMapper borrowingMapper,
                                   LogService logService) {
        this.borrowingRepository = borrowingRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.borrowingMapper = borrowingMapper;
        this.logService = logService;
    }

    // Récupération des livres empruntés
    @Override
    public ServiceResponse<List<BorrowingDTO>> getAllBorrowings() {
        try {
            List<Borrowing> borrowings = borrowingRepository.findAll();

            if (borrowings.isEmpty()) {
                logService.info("Aucun emprunt trouvé.");
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NO_CONTENT,
                        ResponseMessage.NO_BORROWINGS,
                        null,
                        false,
                        "Aucun emprunt trouvé"
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
                    "Récupération de {} emprunts avec succès",
                    borrowingDTOs.size()
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la récupération des emprunts");
        }
    }

    // Récupération d'un emprunt par ID
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
                        true,
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
                        ResponseMessage.NO_BORROWER_FOR_USERNAME,
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
                        ResponseMessage.NO_BORROWER_FOR_BOOK,
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
                        true,
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
                        true,
                        "Le livre '{}' a déjà été retourné",
                        borrowedBook.getTitle()
                );
            }

            // Mettre à jour la date de retour avant suppression
            borrowing.setReturnDate(LocalDate.now());
            borrowingRepository.save(borrowing); // Optionnel : garder l'historique

            // Mise à jour du statut du livre
            borrowedBook.setStatus(BookStatus.AVAILABLE);
            borrowedBook.setBorrowerUsername(null);
            bookRepository.save(borrowedBook);

            // Supprimer l'emprunt (ou le garder pour l'historique)
            borrowingRepository.delete(borrowing);

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BOOK_RETURN_SUCCESS,
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
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.NO_BORROWER_FOR_CATEGORY,
                        null,
                        false,
                        "Aucun livre trouvé pour la catégorie avec l'ID '{}'.",
                        categoryId
                );
            }

            List<Borrowing> borrowings = borrowingRepository.findByBookIn(books);

            if (borrowings.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NO_CONTENT,
                        ResponseMessage.NO_BORROWER_FOR_CATEGORY,
                        null,
                        false,
                        "Aucun emprunt trouvé pour les livres de la catégorie avec l'ID '{}'.",
                        categoryId
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
                    "Emprunts récupérés pour la catégorie avec l'ID '{}'.",
                    categoryId
            );
        } catch (Exception e) {
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
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.NO_AVAILABLE_BOOKS,
                        null,
                        false,
                        "Aucun livre trouvé avec le statut '{}'.",
                        status
                );
            }

            List<Borrowing> borrowings = borrowingRepository.findByBookIn(books);

            if (borrowings.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NO_CONTENT,
                        ResponseMessage.NO_BORROWED_BOOKS,
                        null,
                        false,
                        "Aucun emprunt trouvé pour les livres avec le statut '{}'.",
                        status
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
                    "Emprunts récupérés pour les livres avec le statut '{}'.",
                    status
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(
                    logService,
                    e,
                    "Erreur lors de la récupération des emprunts pour les livres avec le statut '{}'",
                    status
            );
        }
    }

    // Création d'un emprunt
    @Transactional
    @Override
    public ServiceResponse<BorrowingDTO> createBorrowing(BorrowingDTO borrowingDTO) {
        try {
            // Vérification des données d'entrée
            if (borrowingDTO == null) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.BORROWING_DTO_NULL,
                        null,
                        true,
                        "Les données d'emprunt sont nulles"
                );
            }

            // Vérification que le livre existe et est disponible
            Optional<Book> bookOpt = bookRepository.findById(borrowingDTO.getBookId());
            if (bookOpt.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.BOOK_NOT_FOUND,
                        null,
                        true,
                        "Livre non trouvé avec l'ID : {}",
                        borrowingDTO.getBookId()
                );
            }

            Book book = bookOpt.get();
            if (book.getStatus() == BookStatus.BORROWED) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.BOOK_ALREADY_BORROWED,
                        null,
                        true,
                        "Le livre '{}' est déjà emprunté",
                        book.getTitle()
                );
            }

            // Initialisation automatique des dates si nécessaires
            if (borrowingDTO.getBorrowDate() == null) {
                borrowingDTO.setBorrowDate(LocalDate.now());
            }
            
            borrowingDTO.setReturnDate(null);

            // Création de l'emprunt via le mapper
            ServiceResponse<Borrowing> mappingResult = borrowingMapper.toEntity(borrowingDTO);
            if (mappingResult.getData() == null) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.BORROWER_ERROR,
                        null,
                        true,
                        "Erreur de mapping des données d'emprunt"
                );
            }

            Borrowing borrowing = mappingResult.getData();
            borrowing.setBook(book);

            // Récupérer l'utilisateur par username
            Optional<User> userOpt = userRepository.findByUsername(borrowingDTO.getUsername());
            
            // Vérifier que l'utilisateur existe
                        if (userOpt.isEmpty()) {
                            return ServiceResponse.logAndRespond(
                                    logService,
                                    ResponseCode.NOT_FOUND,
                                    ResponseMessage.USER_NOT_FOUND,
                                    null,
                                    true,
                                    "Utilisateur non trouvé avec le nom d'utilisateur : {}",
                                    borrowingDTO.getUsername()
                            );
                        }
            
            // Associer l'utilisateur à l'emprunt
            User user = userOpt.get();
                        borrowing.setUser(user);

            // Mise à jour du statut du livre
            book.setStatus(BookStatus.BORROWED);
            book.setBorrowerUsername(borrowingDTO.getUsername());
            
            bookRepository.save(book);
            Borrowing savedBorrowing = borrowingRepository.save(borrowing);
            BorrowingDTO responseDTO = borrowingMapper.toDTO(savedBorrowing);

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.CREATED,
                    ResponseMessage.BOOK_BORROW_SUCCESS,
                    responseDTO,
                    false,
                    "Livre '{}' emprunté avec succès par {}",
                    book.getTitle(),
                    borrowingDTO.getUsername()
            );
        } catch (Exception e) {
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.INTERNAL_ERROR,
                    ResponseMessage.BORROWER_ERROR,
                    null,
                    true,
                    "Erreur lors de la création de l'emprunt : {}",
                    e.getMessage()
            );
        }
    }
}