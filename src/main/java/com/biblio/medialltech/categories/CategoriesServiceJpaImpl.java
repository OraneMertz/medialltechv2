package com.biblio.medialltech.categories;

import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriesServiceJpaImpl implements CategoriesService {

    private final CategoriesRepository categoriesRepository;
    private final CategoriesMapper categoriesMapper;
    private final LogService logService;

    public CategoriesServiceJpaImpl(CategoriesRepository categoriesRepository,
                                    CategoriesMapper categoriesMapper,
                                    LogService logService) {
        this.categoriesRepository = categoriesRepository;
        this.categoriesMapper = categoriesMapper;
        this.logService = logService;
    }

    // Récupération de toutes les catégories
    @Override
    public ServiceResponse<List<CategoriesDTO>> getAllCategories() {
        try {
            List<Categories> categories = categoriesRepository.findAll();

            if (categories.isEmpty()) {
                logService.info("Aucune catégorie trouvée.");
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NO_CONTENT,
                        ResponseMessage.CATEGORY_NO_CONTENT,
                        null,
                        false,
                        "Aucune catégorie trouvée"
                );
            }

            // Conversion de la liste d'entités en liste de DTOs
            List<CategoriesDTO> categoriesDTOs = categories.stream()
                    .map(categoriesMapper::toDTO)
                    .toList();

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.CATEGORY_SUCCESS,
                    categoriesDTOs,
                    false,
                    "Récupération de {} catégorie(s) avec succès",
                    categoriesDTOs.size()
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(
                    logService,
                    e,
                    "Erreur lors de la récupération de toutes les catégories"
            );
        }
    }

    // Récupération des catégories par leurs IDs
    @Override
    public ServiceResponse<CategoriesDTO> getCategoryById(Long id) {
        try {
            Optional<Categories> categoryOpt = categoriesRepository.findById(id);

            if (categoryOpt.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.CATEGORY_NOT_FOUND,
                        null,
                        true,
                        "Catégorie non trouvée avec ID " + id
                );
            }

            Categories categories = categoryOpt.get();
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.CATEGORY_SUCCESS,
                    categoriesMapper.toDTO(categories),
                    false,
                    "Catégorie trouvée avec ID " + id
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(
                    logService,
                    e,
                    "Erreur lors de la récupération de la catégorie avec ID " + id
            );
        }
    }

    // Récupération des catégories par leurs noms
    @Override
    public ServiceResponse<CategoriesDTO> getCategoryByName(String name) {
        try {
            Optional<Categories> categoryOpt = categoriesRepository.findByName(name);

            if (categoryOpt.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.CATEGORY_NOT_FOUND,
                        null,
                        true,
                        "Catégorie non trouvée avec le nom " + name
                );
            }

            Categories categories = categoryOpt.get();
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.CATEGORY_SUCCESS,
                    categoriesMapper.toDTO(categories),
                    false,
                    "Catégorie trouvée avec le nom " + name
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(
                    logService,
                    e,
                    "Erreur lors de la récupération de la catégorie avec le nom " + name
            );
        }
    }

    // Mise à jour des catégories
    @Transactional
    @Override
    public ServiceResponse<CategoriesDTO> updateCategory(Long id, CategoriesDTO categoriesDTO) {
        try {
            // Vérification des données d'entrée
            if (categoriesDTO == null) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.CATEGORY_NULL,
                        null,
                        true,
                        "Les données de catégorie sont nulles"
                );
            }

            Optional<Categories> optional = categoriesRepository.findById(id);

            if (optional.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.CATEGORY_NOT_FOUND,
                        null,
                        true,
                        "Échec mise à jour : ID non trouvé " + id
                );
            }

            Categories existingCategories = optional.get();
            boolean nameAlreadyExists = !categoriesDTO.getName().equals(existingCategories.getName()) &&
                    categoriesRepository.existsByName(categoriesDTO.getName());

            if (nameAlreadyExists) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.ALREADY_EXISTS,
                        ResponseMessage.CATEGORY_ALREADY_EXISTS,
                        null,
                        true,
                        "Échec mise à jour : nom de catégorie déjà existant."
                );
            }

            existingCategories.setName(categoriesDTO.getName());
            Categories updated = categoriesRepository.save(existingCategories);

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.CATEGORY_UPDATED,
                    categoriesMapper.toDTO(updated),
                    false,
                    "Catégorie mise à jour : " + updated.getName()
            );
        } catch (Exception e) {
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.INTERNAL_ERROR,
                    ResponseMessage.CATEGORY_UPDATE_ERROR,
                    null,
                    true,
                    "Erreur lors de la mise à jour de la catégorie avec ID " + id
            );
        }
    }

    // Création d'une catégorie
    @Transactional
    @Override
    public ServiceResponse<CategoriesDTO> createCategory(CategoriesDTO categoriesDTO) {
        try {
            // Vérification des données d'entrée
            if (categoriesDTO == null) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.CATEGORY_NULL,
                        null,
                        true,
                        "Les données de catégorie sont nulles"
                );
            }

            if (categoriesRepository.existsByName(categoriesDTO.getName())) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.ALREADY_EXISTS,
                        ResponseMessage.CATEGORY_ALREADY_EXISTS,
                        null,
                        true,
                        "La catégorie avec le nom '{}' existe déjà.",
                        categoriesDTO.getName()
                );
            }

            Categories categories = categoriesMapper.toEntity(categoriesDTO).getData();
            Categories savedCategories = categoriesRepository.save(categories);

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.CREATED,
                    ResponseMessage.CATEGORY_CREATED,
                    categoriesMapper.toDTO(savedCategories),
                    false,
                    "Catégorie créée avec succès : {}",
                    savedCategories.getName()
            );
        } catch (Exception e) {
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.INTERNAL_ERROR,
                    ResponseMessage.CATEGORY_CREATION_ERROR,
                    null,
                    true,
                    "Erreur lors de la création de la catégorie avec le nom '{}'",
                    categoriesDTO != null ? categoriesDTO.getName() : "null"
            );
        }
    }

    // Suppression d'une catégorie
    @Transactional
    @Override
    public ServiceResponse<Void> deleteCategory(Long id) {
        try {
            if (!categoriesRepository.existsById(id)) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.CATEGORY_NOT_FOUND,
                        null,
                        true,
                        "Échec suppression : Catégorie non trouvée avec ID " + id
                );
            }

            categoriesRepository.deleteById(id);
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.CATEGORY_DELETED,
                    null,
                    false,
                    "Catégorie supprimée avec ID " + id
            );
        } catch (Exception e) {
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.INTERNAL_ERROR,
                    ResponseMessage.DELETE_CATEGORY_ERROR,
                    null,
                    true,
                    "Erreur lors de la suppression de la catégorie avec ID " + id
            );
        }
    }
}