package com.biblio.medialltech.borrowers;

import com.biblio.medialltech.books.BookStatus;
import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BorrowerServiceJpaImpl implements BorrowerService {
    private final BorrowerRepository borrowerRepository;
    private final BorrowerMapper borrowerMapper;
    private final LogService logService;

    public BorrowerServiceJpaImpl(BorrowerRepository borrowerRepository,
                                  BorrowerMapper borrowerMapper,
                                  LogService logService) {
        this.borrowerRepository = borrowerRepository;
        this.borrowerMapper = borrowerMapper;
        this.logService = logService;
    }

    @Override
    public ServiceResponse<List<BorrowerDTO>> getAllBorrowers() {
        try {
            List<Borrower> borrowers = borrowerRepository.findAll();

            if (borrowers.isEmpty()) {
                logService.info("Aucun emprunteur trouvé.");
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NO_CONTENT,
                        ResponseMessage.NO_BORROWINGS,
                        null,
                        false,
                        "Aucun emprunteur trouvé"
                );
            }

            List<BorrowerDTO> borrowerDTOs = borrowers.stream()
                    .map(borrowerMapper::toDTO)
                    .toList(); // ✅ Modernisé

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BORROWER_SUCCESS,
                    borrowerDTOs,
                    false,
                    "Récupération de {} emprunteur(s) avec succès",
                    borrowerDTOs.size()
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la récupération des emprunteurs");
        }
    }

    @Override
    public ServiceResponse<BorrowerDTO> getBorrowerById(Long id) {
        try {
            Optional<Borrower> borrowerOpt = borrowerRepository.findById(id);

            if (borrowerOpt.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.BORROWER_NOT_FOUND,
                        null,
                        true,
                        "L'emprunteur avec l'ID {} n'existe pas",
                        id
                );
            }

            BorrowerDTO borrowerDTO = borrowerMapper.toDTO(borrowerOpt.get());
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BORROWER_SUCCESS,
                    borrowerDTO,
                    false,
                    "Emprunteur récupéré avec succès: {}",
                    borrowerDTO.getBorrowerUsername()
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la récupération de l'emprunteur avec l'ID: {}", id);
        }
    }

    @Override
    public ServiceResponse<List<BorrowerDTO>> getBorrowersByUsername(String username) {
        try {
            List<Borrower> borrowers = borrowerRepository.findByBorrowerUsernameContainingIgnoreCase(username);

            if (borrowers.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.NO_BORROWER_FOR_USERNAME,
                        null,
                        false,
                        "Aucun emprunteur trouvé pour le nom d'utilisateur: '{}'",
                        username
                );
            }

            List<BorrowerDTO> borrowerDTOs = borrowers.stream()
                    .map(borrowerMapper::toDTO)
                    .toList();

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BORROWER_SUCCESS,
                    borrowerDTOs,
                    false,
                    "{} emprunteur(s) trouvé(s) pour le nom d'utilisateur '{}'",
                    borrowerDTOs.size(),
                    username
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la recherche des emprunteurs par nom d'utilisateur: '{}'", username);
        }
    }

    @Override
    public ServiceResponse<List<BorrowerDTO>> getBorrowersByBookId(Long bookId) {
        try {
            List<Borrower> borrowers = borrowerRepository.findByBookId(bookId);

            if (borrowers.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.NO_BORROWER_FOR_BOOK,
                        null,
                        false,
                        "Aucun emprunteur trouvé pour le livre ID: '{}'",
                        bookId
                );
            }

            List<BorrowerDTO> borrowerDTOs = borrowers.stream()
                    .map(borrowerMapper::toDTO)
                    .toList();

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BORROWER_SUCCESS,
                    borrowerDTOs,
                    false,
                    "{} emprunteur(s) trouvé(s) pour le livre ID '{}'",
                    borrowerDTOs.size(),
                    bookId
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la recherche des emprunteurs pour le livre ID: '{}'", bookId);
        }
    }

    @Override
    public ServiceResponse<List<BorrowerDTO>> getBorrowersByCategory(Long categoryId) {
        try {
            List<Borrower> borrowers = borrowerRepository.findByCategoriesId(categoryId);

            if (borrowers.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.NO_BORROWER_FOR_CATEGORY,
                        null,
                        false,
                        "Aucun emprunteur trouvé pour la catégorie ID: '{}'",
                        categoryId
                );
            }

            List<BorrowerDTO> borrowerDTOs = borrowers.stream()
                    .map(borrowerMapper::toDTO)
                    .toList();

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BORROWER_SUCCESS,
                    borrowerDTOs,
                    false,
                    "{} emprunteur(s) trouvé(s) pour la catégorie ID '{}'",
                    borrowerDTOs.size(),
                    categoryId
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la recherche des emprunteurs pour la catégorie ID: '{}'", categoryId);
        }
    }

    @Transactional
    @Override
    public ServiceResponse<BorrowerDTO> createBorrower(BorrowerDTO borrowerDTO) {
        try {
            // Vérification des données d'entrée
            if (borrowerDTO == null) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.BORROWING_DTO_NULL,
                        null,
                        true,
                        "Les données d'emprunteur sont nulles"
                );
            }

            ServiceResponse<Borrower> mappingResult = borrowerMapper.toEntity(borrowerDTO);

            if (mappingResult.isError() || mappingResult.getData() == null) {
                return ServiceResponse.logAndRespond(
                        logService,
                        mappingResult.getStatusCode(),
                        mappingResult.getMessage(),
                        null,
                        true,
                        "Échec du mapping BorrowerDTO vers Borrower pour l'utilisateur '{}'",
                        borrowerDTO.getBorrowerUsername()
                );
            }

            Borrower savedBorrower = borrowerRepository.save(mappingResult.getData());
            BorrowerDTO savedBorrowerDTO = borrowerMapper.toDTO(savedBorrower);

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.CREATED,
                    ResponseMessage.BORROWER_CREATED,
                    savedBorrowerDTO,
                    false,
                    "Emprunteur créé avec succès: {}",
                    savedBorrowerDTO.getBorrowerUsername()
            );
        } catch (Exception e) {
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.INTERNAL_ERROR,
                    ResponseMessage.BORROWER_ERROR,
                    null,
                    true,
                    "Erreur lors de la création de l'emprunteur '{}'",
                    borrowerDTO != null ? borrowerDTO.getBorrowerUsername() : "null"
            );
        }
    }

    @Transactional
    @Override
    public ServiceResponse<BorrowerDTO> updateBorrower(Long id, BorrowerDTO borrowerDTO) {
        try {
            // Vérification des données d'entrée
            if (borrowerDTO == null) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.BORROWING_DTO_NULL,
                        null,
                        true,
                        "Les données d'emprunteur sont nulles"
                );
            }

            Optional<Borrower> existingBorrowerOpt = borrowerRepository.findById(id);

            if (existingBorrowerOpt.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.BORROWER_NOT_FOUND,
                        null,
                        true,
                        "L'emprunteur avec l'ID '{}' n'a pas été trouvé",
                        id
                );
            }

            Borrower existingBorrower = existingBorrowerOpt.get();

            // ✅ UTILISATION DE updateEntityFromDTO
            borrowerMapper.updateEntityFromDTO(existingBorrower, borrowerDTO);

            Borrower updatedBorrower = borrowerRepository.save(existingBorrower);
            BorrowerDTO updatedBorrowerDTO = borrowerMapper.toDTO(updatedBorrower);

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BORROWER_SUCCESS,
                    updatedBorrowerDTO,
                    false,
                    "L'emprunteur avec l'ID '{}' a été mis à jour avec succès",
                    updatedBorrower.getId()
            );
        } catch (Exception e) {
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.INTERNAL_ERROR,
                    ResponseMessage.BORROWER_ERROR,
                    null,
                    true,
                    "Erreur lors de la mise à jour de l'emprunteur avec l'ID '{}'",
                    id
            );
        }
    }

    @Override
    public ServiceResponse<Void> deleteBorrower(Long id) {
        try {
            if (!borrowerRepository.existsById(id)) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.BORROWER_NOT_FOUND,
                        null,
                        true,
                        "L'emprunteur avec l'ID '{}' n'a pas été trouvé",
                        id
                );
            }

            borrowerRepository.deleteById(id);

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BORROWER_DELETED,
                    null,
                    false,
                    "L'emprunteur avec l'ID '{}' a été supprimé avec succès",
                    id
            );
        } catch (Exception e) {
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.INTERNAL_ERROR,
                    ResponseMessage.BORROWER_ERROR,
                    null,
                    true,
                    "Erreur lors de la suppression de l'emprunteur avec l'ID '{}'",
                    id
            );
        }
    }
    
    @Override
    public ServiceResponse<List<BorrowerDTO>> getBorrowersByStatus(BookStatus status) {
        try {
            List<Borrower> borrowers = borrowerRepository.findByStatus(status);

            if (borrowers.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.NO_BORROWINGS,
                        null,
                        false,
                        "Aucun emprunteur trouvé avec le statut: '{}'",
                        status
                );
            }

            List<BorrowerDTO> borrowerDTOs = borrowers.stream()
                    .map(borrowerMapper::toDTO)
                    .toList();

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BORROWER_SUCCESS,
                    borrowerDTOs,
                    false,
                    "{} emprunteur(s) trouvé(s) avec le statut '{}'",
                    borrowerDTOs.size(),
                    status
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la recherche des emprunteurs par statut: '{}'", status);
        }
    }
    
    @Override
    public ServiceResponse<BorrowerDTO> getBorrowerByExactUsername(String username) {
        try {
            List<Borrower> borrowers = borrowerRepository.findByBorrowerUsername(username);

            if (borrowers.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.NO_BORROWER_FOR_USERNAME,
                        null,
                        false,
                        "Aucun emprunteur trouvé avec le nom d'utilisateur exact: '{}'",
                        username
                );
            }

            BorrowerDTO borrowerDTO = borrowerMapper.toDTO(borrowers.getFirst());

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BORROWER_SUCCESS,
                    borrowerDTO,
                    false,
                    "Emprunteur trouvé avec le nom d'utilisateur exact: '{}'",
                    username
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la recherche de l'emprunteur par nom d'utilisateur exact: '{}'", username);
        }
    }
}