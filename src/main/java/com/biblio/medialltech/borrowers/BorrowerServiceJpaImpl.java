package com.biblio.medialltech.borrowers;

import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            List<BorrowerDTO> borrowers = borrowerRepository.findAll().stream()
                    .map(borrowerMapper::toDTO)
                    .collect(Collectors.toList());

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BORROWER_SUCCESS,
                    borrowers,
                    false,
                    "Récupération de {} emprunteur(s) avec succès",
                    borrowers.size()
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
                        false,
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
            List<BorrowerDTO> borrowers = borrowerRepository.findByBorrowerUsernameContainingIgnoreCase(username).stream()
                    .map(borrowerMapper::toDTO)
                    .toList();

            if (borrowers.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NO_CONTENT,
                        ResponseMessage.NO_BORROWER_FOR_USERNAME,
                        borrowers,
                        false,
                        "Aucun emprunteur trouvé pour le nom d'utilisateur: '{}'",
                        username
                );
            }

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BORROWER_SUCCESS,
                    borrowers,
                    false,
                    "{} emprunteur(s) trouvé(s) pour le nom d'utilisateur '{}'",
                    borrowers.size(),
                    username
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la recherche des emprunteurs par nom d'utilisateur: '{}'", username);
        }
    }

    @Override
    public ServiceResponse<List<BorrowerDTO>> getBorrowersByBookId(Long bookId) {
        try {
            List<BorrowerDTO> borrowers = borrowerRepository.findByBookId(bookId).stream()
                    .map(borrowerMapper::toDTO)
                    .toList();

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

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BORROWER_SUCCESS,
                    borrowers,
                    false,
                    "{} emprunteur(s) trouvé(s) pour le livre ID '{}'",
                    borrowers.size(),
                    bookId
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(logService, e, "Erreur lors de la recherche des emprunteurs pour le livre ID: '{}'", bookId);
        }
    }

    @Override
    public ServiceResponse<List<BorrowerDTO>> getBorrowersByCategory(Long categoryId) {
        try {
            List<BorrowerDTO> borrowers = borrowerRepository.findByCategoriesId(categoryId).stream()
                    .map(borrowerMapper::toDTO)
                    .toList();

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

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.BORROWER_SUCCESS,
                    borrowers,
                    false,
                    "{} emprunteur(s) trouvé(s) pour la catégorie ID '{}'",
                    borrowers.size(),
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
            ServiceResponse<Borrower> mappingResult = borrowerMapper.toEntity(borrowerDTO);

            if (mappingResult.getData() == null) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.INTERNAL_ERROR,
                        ResponseMessage.UNEXPECTED_ERROR,
                        null,
                        false,
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
            return ServiceResponse.handleException(logService, e, "Erreur lors de la création de l'emprunteur '{}'", borrowerDTO.getBorrowerUsername());
        }
    }

    @Transactional
    @Override
    public ServiceResponse<BorrowerDTO> updateBorrower(Long id, BorrowerDTO borrowerDTO) {
        try {
            Optional<Borrower> existingBorrowerOpt = borrowerRepository.findById(id);

            if (existingBorrowerOpt.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.BORROWER_NOT_FOUND,
                        null,
                        false,
                        "L'emprunteur avec l'ID '{}' n'a pas été trouvé",
                        id
                );
            }

            ServiceResponse<Borrower> mappingResult = borrowerMapper.toEntity(borrowerDTO);

            if (mappingResult.getData() == null) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.DATA_HANDLING_ERROR,
                        ResponseMessage.DATA_HANDLING_ERROR,
                        null,
                        false,
                        "Le mappage du BorrowerDTO pour l'ID '{}' a échoué. Données invalides",
                        id
                );
            }

            Borrower existingBorrower = existingBorrowerOpt.get();
            Borrower borrowerToUpdate = mappingResult.getData();
            borrowerToUpdate.setId(existingBorrower.getId());

            Borrower updatedBorrower = borrowerRepository.save(borrowerToUpdate);
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
            return ServiceResponse.handleException(logService, e, "Erreur lors de la mise à jour de l'emprunteur avec l'ID '{}'", id);
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
                        false,
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
            return ServiceResponse.handleException(logService, e, "Erreur lors de la suppression de l'emprunteur avec l'ID '{}'", id);
        }
    }
}