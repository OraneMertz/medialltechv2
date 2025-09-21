package com.biblio.medialltech.books;

import com.biblio.medialltech.categories.Category;
import com.biblio.medialltech.categories.CategoryRepository;
import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;
    private final LogService logService;

    public BookServiceImpl(BookRepository bookRepository,
                           CategoryRepository categoryRepository,
                           BookMapper bookMapper,
                           LogService logService) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.bookMapper = bookMapper;
        this.logService = logService;
    }

    @Override
    public List<BookDTO> getAllBooks() {
        try {
            List<Book> books = bookRepository.findAll();

            List<BookDTO> bookDTOs = books.stream()
                    .map(bookMapper::toDTO)
                    .collect(Collectors.toList());

            logService.info("Récupération de {} livres avec succès", bookDTOs.size());
            return bookDTOs;
        } catch (Exception e) {
            ServiceResponse.handleException(logService, e, "Erreur lors de la récupération des livres");
            return new ArrayList<>();
        }
    }

    @Override
    public BookDTO getBookById(String id) { // Changé de Long à String
        try {
            Optional<Book> bookOpt = bookRepository.findById(id);

            if (bookOpt.isEmpty()) {
                logService.warn("L'id {} n'existe pas", id);
                return null;
            }

            BookDTO bookDTO = bookMapper.toDTO(bookOpt.get());
            logService.info("Livre récupéré avec succès: {}", bookDTO.getTitle());
            return bookDTO;

        } catch (Exception e) {
            ServiceResponse.handleException(logService, e, "Erreur lors de la récupération du livre avec l'ID: {}", id);
            return null;
        }
    }

    @Transactional // MongoDB supporte les transactions depuis la v4.0
    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        try {
            // Vérification des données d'entrée
            if (bookDTO == null) {
                logService.warn("Les données de livre sont nulles");
                return null;
            }

            // Définir le statut par défaut si non spécifié
            if (bookDTO.getStatus() == null) {
                bookDTO.setStatus(BookStatus.AVAILABLE);
            }

            ServiceResponse<Book> mappingResult = bookMapper.toEntity(bookDTO);

            if (mappingResult.getData() == null) {
                logService.error("Échec du mapping BookDTO vers Book pour le titre '{}'", bookDTO.getTitle());
                return null;
            }

            Book book = mappingResult.getData();

            if (!isValidImageUrl(book.getImage())) {
                logService.warn("L'URL de l'image fournie n'est pas valide pour le livre '{}'", book.getTitle());
                return null;
            }
            book.setImage(book.getImage().trim());

            // Associer les catégories valides au livre envoyé
            if (bookDTO.getCategoryIds() != null && !bookDTO.getCategoryIds().isEmpty()) {
                ServiceResponse<List<Category>> categoriesResult = validateAndGetCategories(bookDTO.getCategoryIds());
                if (categoriesResult.isError()) {
                    logService.error("Erreur lors de la validation des catégories");
                    return null;
                }

                book.setCategories(categoriesResult.getData());
                logService.info("Catégories associées au livre '{}' : {}",
                        bookDTO.getTitle(), bookDTO.getCategoryIds());
            } else {
                logService.info("Aucune catégorie spécifiée pour le livre '{}'", bookDTO.getTitle());
            }

            Book savedBook = bookRepository.save(book);
            BookDTO savedBookDTO = bookMapper.toDTO(savedBook);

            logService.info("Livre créé avec succès: {}", savedBookDTO.getTitle());
            return savedBookDTO;

        } catch (Exception e) {
            ServiceResponse.handleException(logService, e, "Erreur lors de la création du livre '{}'",
                    bookDTO != null ? bookDTO.getTitle() : "null");
            return null;
        }
    }

    private boolean isValidImageUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        try {
            java.net.URI uri = java.net.URI.create(url);
            if (uri.getScheme() == null || !uri.getScheme().equals("https")) {
                return false;
            }

            // Vérification optionnelle des extensions d'image courantes
            String lowerUrl = url.toLowerCase();
            return lowerUrl.endsWith(".jpg") || lowerUrl.endsWith(".jpeg") ||
                    lowerUrl.endsWith(".png") ||
                    lowerUrl.endsWith(".webp") || lowerUrl.endsWith(".svg");
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Transactional
    @Override
    public BookDTO updateBook(String id, BookDTO bookDTO) { // Changé de Long à String
        try {
            // Vérification des données d'entrée
            if (bookDTO == null) {
                logService.warn("Les données de livre sont nulles");
                return null;
            }

            Optional<Book> existingBookOpt = bookRepository.findById(id);

            if (existingBookOpt.isEmpty()) {
                logService.warn("Le livre avec l'ID '{}' n'a pas été trouvé", id);
                return null;
            }

            Book existingBook = existingBookOpt.get();

            // Utiliser la méthode du mapper pour mettre à jour
            bookMapper.updateEntityFromDTO(existingBook, bookDTO);

            // Mettre à jour les catégories si nécessaire
            if (bookDTO.getCategoryIds() != null) {
                if (bookDTO.getCategoryIds().isEmpty()) {
                    // Supprimer toutes les catégories
                    existingBook.setCategories(new ArrayList<>());
                    logService.info("Toutes les catégories supprimées du livre ID : {}", id);
                } else {
                    // Associer les nouvelles catégories
                    ServiceResponse<List<Category>> categoriesResult = validateAndGetCategories(bookDTO.getCategoryIds());
                    if (categoriesResult.isError()) {
                        logService.error("Erreur lors de la validation des catégories");
                        return null;
                    }

                    existingBook.setCategories(categoriesResult.getData());
                    logService.info("Catégories mises à jour pour le livre ID {} : {}",
                            id, bookDTO.getCategoryIds());
                }
            }

            Book updatedBook = bookRepository.save(existingBook);
            BookDTO updatedBookDTO = bookMapper.toDTO(updatedBook);

            logService.info("Le livre avec l'ID '{}' a été mis à jour avec succès", updatedBook.getId());
            return updatedBookDTO;

        } catch (Exception e) {
            ServiceResponse.handleException(logService, e, "Erreur lors de la mise à jour du livre avec l'ID '{}'", id);
            return null;
        }
    }

    @Override
    public String deleteBook(String id) { // Changé de Long à String
        try {
            if (!bookRepository.existsById(id)) {
                ServiceResponse.handleException(logService, null, "Le livre avec l'ID '{}' n'a pas été trouvé", id);
                return null;
            }

            bookRepository.deleteById(id);
            logService.info("Le livre avec l'ID '{}' a été supprimé avec succès", id);
            return id;
        } catch (Exception e) {
            ServiceResponse.handleException(logService, e, "Erreur lors de la suppression du livre avec l'ID '{}'", id);
            return null;
        }
    }

    /**
     * Méthode privée pour valider et récupérer les catégories par leurs IDs
     *
     * @param categoryIds Liste des IDs des catégories (String maintenant)
     * @return ServiceResponse contenant la liste des catégories ou une erreur
     */
    private ServiceResponse<List<Category>> validateAndGetCategories(List<String> categoryIds) {
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
        List<Category> categories = categoryRepository.findAllById(categoryIds);

        // Vérifier que toutes les catégories existent
        if (categories.size() != categoryIds.size()) {
            List<String> foundIds = categories.stream()
                    .map(Category::getId)
                    .toList();

            List<String> missingIds = categoryIds.stream()
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