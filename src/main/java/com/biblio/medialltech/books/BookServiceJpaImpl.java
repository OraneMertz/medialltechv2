package com.biblio.medialltech.books;

import com.biblio.medialltech.categories.Categories;
import com.biblio.medialltech.categories.CategoriesRepository;
import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceJpaImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoriesRepository categoriesRepository;
    private final BookMapper bookMapper;
    private final LogService logService;

    public BookServiceJpaImpl(BookRepository bookRepository,
                              CategoriesRepository categoriesRepository,
                              BookMapper bookMapper,
                              LogService logService) {
        this.bookRepository = bookRepository;
        this.categoriesRepository = categoriesRepository;
        this.bookMapper = bookMapper;
        this.logService = logService;
    }

    @Override
    public ServiceResponse<List<BookDTO>> getAllBooks() {
        try {
            List<Book> books = bookRepository.findAll();

            if (books.isEmpty()) {
                logService.info("Aucun livre trouvé.");
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NO_CONTENT,
                        ResponseMessage.BOOK_NO_CONTENT,
                        null,
                        false,
                        "Aucun livre trouvé"
                );
            }

            List<BookDTO> bookDTOs = books.stream()
                    .map(bookMapper::toDTO)
                    .collect(Collectors.toList());

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BOOK_SUCCESS,
                    bookDTOs,
                    false,
                    "Récupération de {} livres avec succès",
                    bookDTOs.size()
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la récupération des livres");
        }
    }

    @Override
    public ServiceResponse<BookDTO> getBookById(Long id) {
        try {
            Optional<Book> bookOpt = bookRepository.findById(id);

            if (bookOpt.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.BOOK_NOT_FOUND,
                        null,
                        true,
                        "L'id {} n'existe pas",
                        id
                );
            }

            BookDTO bookDTO = bookMapper.toDTO(bookOpt.get());
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BOOK_SUCCESS,
                    bookDTO,
                    false,
                    "Livre récupéré avec succès: {}",
                    bookDTO.getTitle()
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la récupération du livre avec l'ID: {}", id);
        }
    }

    @Override
    public ServiceResponse<List<BookDTO>> getBooksByAuthor(String author) {
        try {
            List<Book> books = bookRepository.findByAuthorContainingIgnoreCase(author);

            if (books.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.NO_BOOKS_FOR_AUTHOR,
                        null,
                        false,
                        "Aucun livre trouvé pour l'auteur: '{}'",
                        author
                );
            }

            List<BookDTO> bookDTOs = books.stream()
                    .map(bookMapper::toDTO)
                    .toList();

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.AUTHOR_SUCCESS,
                    bookDTOs,
                    false,
                    "{} livre(s) trouvé(s) pour l'auteur '{}'",
                    bookDTOs.size(),
                    author
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la recherche des livres par auteur: '{}'", author);
        }
    }

    @Override
    public ServiceResponse<List<BookDTO>> getBookByCategory(Long categoryId) {
        try {
            List<Book> books = bookRepository.findByCategoriesId(categoryId);

            if (books.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.NO_BOOKS_FOR_AUTHOR, // Réutilisation du message générique "pas de livre"
                        null,
                        false,
                        "Aucun livre trouvé pour la catégorie ID: '{}'",
                        categoryId
                );
            }

            List<BookDTO> bookDTOs = books.stream()
                    .map(bookMapper::toDTO)
                    .toList();

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BOOK_SUCCESS,
                    bookDTOs,
                    false,
                    "{} livre(s) trouvé(s) pour la catégorie ID '{}'",
                    bookDTOs.size(),
                    categoryId
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la recherche des livres pour la catégorie ID: '{}'", categoryId);
        }
    }

    /**
     * Récupère tous les livres par leurs emprunteurs
     */
    @Override
    public ServiceResponse<List<BookDTO>> getBooksByBorrower(String borrowerUsername) {
        try {
            List<Book> books = bookRepository.findByBorrowerUsername(borrowerUsername);

            if (books.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.NO_BOOKS_FOR_BORROWER,
                        null,
                        false,
                        "Aucun livre trouvé pour l'emprunteur: '{}'",
                        borrowerUsername
                );
            }

            List<BookDTO> bookDTOs = books.stream()
                    .map(bookMapper::toDTO)
                    .collect(Collectors.toList());

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BOOK_SUCCESS,
                    bookDTOs,
                    false,
                    "{} livre(s) trouvé(s) pour l'emprunteur '{}'",
                    bookDTOs.size(),
                    borrowerUsername
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la récupération des livres pour l'emprunteur '{}'", borrowerUsername);
        }
    }

    @Override
    public ServiceResponse<List<BookDTO>> getAvailableBooks() {
        try {
            List<Book> availableBooks = bookRepository.findByStatus(BookStatus.AVAILABLE);

            if (availableBooks.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.NO_AVAILABLE_BOOKS,
                        null,
                        false,
                        "Aucun livre disponible trouvé"
                );
            }

            List<BookDTO> availableBookDTOs = availableBooks.stream()
                    .map(bookMapper::toDTO)
                    .collect(Collectors.toList());

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BOOK_SUCCESS,
                    availableBookDTOs,
                    false,
                    "{} livre(s) disponible(s) récupéré(s)",
                    availableBookDTOs.size()
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la récupération des livres disponibles");
        }
    }

    @Transactional
    @Override
    public ServiceResponse<BookDTO> createBook(BookDTO bookDTO) {
        try {
            // Vérification des données d'entrée
            if (bookDTO == null) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.BOOK_NULL,
                        null,
                        true,
                        "Les données de livre sont nulles"
                );
            }

            // Définir le statut par défaut si non spécifié
            if (bookDTO.getStatus() == null) {
                bookDTO.setStatus(BookStatus.AVAILABLE);
            }

            ServiceResponse<Book> mappingResult = bookMapper.toEntity(bookDTO);

            if (mappingResult.getData() == null) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.INTERNAL_ERROR,
                        ResponseMessage.BOOK_CREATION_ERROR,
                        null,
                        true,
                        "Échec du mapping BookDTO vers Book pour le titre '{}'",
                        bookDTO.getTitle()
                );
            }

            Book book = mappingResult.getData();

            // Associer les catégories si des categoryIds sont fournis
            if (bookDTO.getCategoryIds() != null && !bookDTO.getCategoryIds().isEmpty()) {
                ServiceResponse<List<Categories>> categoriesResult = validateAndGetCategories(bookDTO.getCategoryIds());
                if (categoriesResult.isError()) {
                    return ServiceResponse.logAndRespond(
                            logService,
                            categoriesResult.getStatusCode(),
                            categoriesResult.getMessage(),
                            null,
                            true,
                            "Erreur lors de la validation des catégories"
                    );
                }

                book.setCategories(categoriesResult.getData());
                logService.info("Catégories associées au livre '{}' : {}",
                        bookDTO.getTitle(), bookDTO.getCategoryIds());
            } else {
                logService.info("Aucune catégorie spécifiée pour le livre '{}'", bookDTO.getTitle());
            }

            Book savedBook = bookRepository.save(book);
            BookDTO savedBookDTO = bookMapper.toDTO(savedBook);

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.CREATED,
                    ResponseMessage.BOOK_CREATED,
                    savedBookDTO,
                    false,
                    "Livre créé avec succès: {}",
                    savedBookDTO.getTitle()
            );
        } catch (Exception e) {
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.INTERNAL_ERROR,
                    ResponseMessage.BOOK_CREATION_ERROR,
                    null,
                    true,
                    "Erreur lors de la création du livre '{}'",
                    bookDTO != null ? bookDTO.getTitle() : "null"
            );
        }
    }

    @Transactional
    @Override
    public ServiceResponse<BookDTO> addImageToBook(Long bookId, String imageUrl) {
        try {
            Optional<Book> bookOpt = bookRepository.findById(bookId);

            if (bookOpt.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.BOOK_NOT_FOUND,
                        null,
                        true,
                        "Le livre avec l'ID '{}' n'a pas été trouvé",
                        bookId
                );
            }

            Book book = bookOpt.get();

            if (imageUrl == null || imageUrl.trim().isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.IMAGE_URL_EMPTY,
                        null,
                        true,
                        "L'URL de l'image ne peut pas être vide pour le livre '{}'",
                        book.getTitle()
                );
            }

            if (!isValidImageUrl(imageUrl.trim())) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.INVALID_FILE,
                        null,
                        true,
                        "L'URL de l'image fournie n'est pas valide pour le livre '{}'",
                        book.getTitle()
                );
            }

            book.setImage(imageUrl.trim());
            bookRepository.save(book);
            BookDTO bookDTO = bookMapper.toDTO(book);

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.IMAGE_SUCCESS,
                    bookDTO,
                    false,
                    "L'image du livre '{}' a été mise à jour avec succès",
                    book.getTitle()
            );
        } catch (Exception e) {
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.INTERNAL_ERROR,
                    ResponseMessage.IMAGE_UPDATE_ERROR,
                    null,
                    true,
                    "Erreur lors de la mise à jour de l'image du livre ID '{}'",
                    bookId
            );
        }
    }

    // Méthode utilitaire pour valider l'URL de l'image (optionnelle)
    private boolean isValidImageUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }

        try {
            java.net.URI uri = java.net.URI.create(url);
            if (uri.getScheme() == null || (!uri.getScheme().equals("http") && !uri.getScheme().equals("https"))) {
                return false;
            }

            // Vérification optionnelle des extensions d'image courantes
            String lowerUrl = url.toLowerCase();
            return lowerUrl.endsWith(".jpg") || lowerUrl.endsWith(".jpeg") ||
                    lowerUrl.endsWith(".png") || lowerUrl.endsWith(".gif") ||
                    lowerUrl.endsWith(".webp") || lowerUrl.endsWith(".svg");

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Transactional
    @Override
    public ServiceResponse<BookDTO> updateBook(Long id, BookDTO bookDTO) {
        try {
            // Vérification des données d'entrée
            if (bookDTO == null) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.BOOK_NULL,
                        null,
                        true,
                        "Les données de livre sont nulles"
                );
            }

            Optional<Book> existingBookOpt = bookRepository.findById(id);

            if (existingBookOpt.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.BOOK_NOT_FOUND,
                        null,
                        true,
                        "Le livre avec l'ID '{}' n'a pas été trouvé",
                        id
                );
            }

            Book existingBook = existingBookOpt.get();

            // Utiliser la méthode du mapper pour mettre à jour
            bookMapper.updateEntityFromDTO(existingBook, bookDTO);

            // Mettre à jour les catégories si nécessaire
            if (bookDTO.getCategoryIds() != null) {
                if (bookDTO.getCategoryIds().isEmpty()) {
                    // Supprimer toutes les catégories
                    existingBook.setCategories(null);
                    logService.info("Toutes les catégories supprimées du livre ID : {}", id);
                } else {
                    // Associer les nouvelles catégories
                    ServiceResponse<List<Categories>> categoriesResult = validateAndGetCategories(bookDTO.getCategoryIds());
                    if (categoriesResult.isError()) {
                        return ServiceResponse.logAndRespond(
                                logService,
                                categoriesResult.getStatusCode(),
                                categoriesResult.getMessage(),
                                null,
                                true,
                                "Erreur lors de la validation des catégories"
                        );
                    }

                    existingBook.setCategories(categoriesResult.getData());
                    logService.info("Catégories mises à jour pour le livre ID {} : {}",
                            id, bookDTO.getCategoryIds());
                }
            }

            Book updatedBook = bookRepository.save(existingBook);
            BookDTO updatedBookDTO = bookMapper.toDTO(updatedBook);

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BOOK_UPDATED,
                    updatedBookDTO,
                    false,
                    "Le livre avec l'ID '{}' a été mis à jour avec succès",
                    updatedBook.getId()
            );
        } catch (Exception e) {
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.INTERNAL_ERROR,
                    ResponseMessage.BOOK_UPDATE_ERROR,
                    null,
                    true,
                    "Erreur lors de la mise à jour du livre avec l'ID '{}'",
                    id
            );
        }
    }

    @Override
    public ServiceResponse<Void> deleteBook(Long id) {
        try {
            if (!bookRepository.existsById(id)) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.BOOK_NOT_FOUND,
                        null,
                        true,
                        "Le livre avec l'ID '{}' n'a pas été trouvé",
                        id
                );
            }

            bookRepository.deleteById(id);

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BOOK_DELETED,
                    null,
                    false,
                    "Le livre avec l'ID '{}' a été supprimé avec succès",
                    id
            );
        } catch (Exception e) {
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.INTERNAL_ERROR,
                    ResponseMessage.DELETE_BOOK_ERROR,
                    null,
                    true,
                    "Erreur lors de la suppression du livre avec l'ID '{}'",
                    id
            );
        }
    }

    /**
     * Méthode privée pour valider et récupérer les catégories par leurs IDs
     * @param categoryIds Liste des IDs des catégories
     * @return ServiceResponse contenant la liste des catégories ou une erreur
     */
    private ServiceResponse<List<Categories>> validateAndGetCategories(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.BAD_REQUEST,
                    ResponseMessage.INVALID_INPUT,
                    null,
                    true,
                    "Liste des IDs de catégories vide ou nulle"
            );
        }

        // Récupérer les catégories par IDs
        List<Categories> categories = categoriesRepository.findAllById(categoryIds);

        // Vérifier que toutes les catégories existent
        if (categories.size() != categoryIds.size()) {
            List<Long> foundIds = categories.stream()
                    .map(Categories::getId)
                    .toList();

            List<Long> missingIds = categoryIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toList());

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.NOT_FOUND,
                    ResponseMessage.CATEGORY_NOT_FOUND,
                    null,
                    true,
                    "Catégories non trouvées avec les IDs : {}",
                    missingIds
            );
        }

        // Succès : toutes les catégories existent
        return ServiceResponse.logAndRespond(
                logService,
                ResponseCode.SUCCESS,
                ResponseMessage.CATEGORY_SUCCESS,
                categories,
                false,
                "Validation réussie pour {} catégorie(s)",
                categories.size()
        );
    }
}