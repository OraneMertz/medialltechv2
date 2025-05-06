package com.biblio.medialltech.books;

import com.biblio.medialltech.blobstorage.AzureBlobStorageService;
import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceJpaImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final LogService logService;
    private final AzureBlobStorageService azureBlobStorageService;

    public BookServiceJpaImpl(BookRepository bookRepository,
                              BookMapper bookMapper,
                              LogService logService,
                              AzureBlobStorageService azureBlobStorageService) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.logService = logService;
        this.azureBlobStorageService = azureBlobStorageService;
    }

    @Override
    public ServiceResponse<List<BookDTO>> getAllBooks() {
        try {
            List<BookDTO> books = bookRepository.findAll().stream()
                    .map(bookMapper::toDTO)
                    .collect(Collectors.toList());

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BOOK_SUCCESS,
                    books,
                    false,
                    "Récupération de {} livres avec succès",
                    books.size()
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
                        false,
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
            List<BookDTO> books = bookRepository.findByAuthorContainingIgnoreCase(author).stream()
                    .map(bookMapper::toDTO)
                    .toList();

            if (books.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NO_CONTENT,
                        ResponseMessage.BOOK_NULL,
                        books,
                        false,
                        "Aucun livre trouvé pour l'auteur: '{}'",
                        author
                );
            }

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.AUTHOR_SUCCESS,
                    books,
                    false,
                    "%d livre(s) trouvé(s) pour l'auteur '%s'",
                    books.size(),
                    author
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la recherche des livres par auteur: '{}'", author);
        }
    }

    @Override
    public ServiceResponse<List<BookDTO>> getBookByCategory(Long categoryId) {
        try {
            List<BookDTO> books = bookRepository.findByCategoriesId(categoryId).stream()
                    .map(bookMapper::toDTO)
                    .toList();

            if (books.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.CATEGORY_NOT_FOUND,
                        null,
                        false,
                        "Aucun livre trouvé pour la catégorie ID: '{}'",
                        categoryId
                );
            }

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.CATEGORY_SUCCESS,
                    books,
                    false,
                    "{} livre(s) trouvé(s) pour la catégorie ID '{}'",
                    books.size(),
                    categoryId
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la recherche des livres pour la catégorie ID: '{}'", categoryId);
        }
    }

    @Override
    public ServiceResponse<List<BookDTO>> getBooksByBorrower(String borrowerUsername) {
        try {
            List<Book> books = bookRepository.findByBorrowerUsername(borrowerUsername);

            if (books.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.BOOK_NULL,
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
            ServiceResponse<Book> mappingResult = bookMapper.toEntity(bookDTO);

            if (mappingResult.getData() == null) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.INTERNAL_ERROR,
                        ResponseMessage.BOOK_CREATION_ERROR,
                        null,
                        false,
                        "Échec du mapping BookDTO vers Book pour le titre '{}'",
                        bookDTO.getTitle()
                );
            }

            Book savedBook = bookRepository.save(mappingResult.getData());
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
            return ServiceResponse.handleException(logService, e, "Erreur lors de la création du livre '{}'", bookDTO.getTitle());
        }
    }

    @Transactional
    @Override
    public ServiceResponse<BookDTO> addImageToBook(Long bookId, MultipartFile file) {
        try {
            Optional<Book> bookOpt = bookRepository.findById(bookId);

            if (bookOpt.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.BOOK_NOT_FOUND,
                        null,
                        false,
                        "Le livre avec l'ID '{}' n'a pas été trouvé",
                        bookId
                );
            }

            Book book = bookOpt.get();

            // Appel à Azure pour uploader l'image
            ServiceResponse<String> uploadResponse = azureBlobStorageService.uploadImage(file);

            if (uploadResponse.getStatusCode() != ResponseCode.SUCCESS) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.INTERNAL_ERROR,
                        ResponseMessage.IMAGE_UPLOAD_ERROR,
                        null,
                        true,
                        "Échec de l'upload de l'image pour le livre '{}'",
                        book.getTitle()
                );
            }

            String imageUrl = uploadResponse.getData();
            if (imageUrl == null || imageUrl.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.INTERNAL_ERROR,
                        ResponseMessage.IMAGE_UPLOAD_ERROR,
                        null,
                        false,
                        "URL de l'image vide après l'upload pour le livre '{}'",
                        book.getTitle()
                );
            }

            // Mise à jour de l'URL de l'image
            book.setImage(imageUrl);
            bookRepository.save(book);
            BookDTO bookDTO = bookMapper.toDTO(book);

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BOOK_SUCCESS,
                    bookDTO,
                    false,
                    "L'image du livre '{}' a été mise à jour avec succès",
                    book.getTitle()
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la mise à jour de l'image du livre ID '{}'", bookId);
        }
    }

    @Transactional
    @Override
    public ServiceResponse<BookDTO> updateBook(Long id, BookDTO bookDTO) {
        try {
            Optional<Book> existingBookOpt = bookRepository.findById(id);

            if (existingBookOpt.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.BOOK_NOT_FOUND,
                        null,
                        false,
                        "Le livre avec l'ID '{}' n'a pas été trouvé",
                        id
                );
            }

            ServiceResponse<Book> mappingResult = bookMapper.toEntity(bookDTO);

            if (mappingResult.getData() == null) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.DATA_HANDLING_ERROR,
                        ResponseMessage.DATA_HANDLING_ERROR,
                        null,
                        false,
                        "Le mappage du BookDTO pour l'ID '{}' a échoué. Données invalides",
                        id
                );
            }

            Book existingBook = existingBookOpt.get();
            Book bookToUpdate = mappingResult.getData();
            bookToUpdate.setId(existingBook.getId());

            Book updatedBook = bookRepository.save(bookToUpdate);
            BookDTO updatedBookDTO = bookMapper.toDTO(updatedBook);

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BOOK_SUCCESS,
                    updatedBookDTO,
                    false,
                    "Le livre avec l'ID '{}' a été mis à jour avec succès",
                    updatedBook.getId()
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la mise à jour du livre avec l'ID '{}'", id);
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
                        false,
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
            return ServiceResponse.handleException(logService, e, "Erreur lors de la suppression du livre avec l'ID '{}'", id);
        }
    }
}
