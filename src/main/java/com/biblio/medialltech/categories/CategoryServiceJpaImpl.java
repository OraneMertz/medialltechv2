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
public class CategoryServiceJpaImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final LogService logService;

    public CategoryServiceJpaImpl(CategoryRepository categoryRepository,
                                  CategoryMapper categoryMapper,
                                  LogService logService) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.logService = logService;
    }

    // Récupération de toutes les catégories
    @Override
    public ServiceResponse<List<CategoryDTO>> getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll();
            logService.info("Récupération de toutes les catégories : " + categories.size() + " trouvées.");
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.CATEGORY_SUCCESS,
                    categoryMapper.toDTOList(categories).getData(),
                    false,
                    "Récupération de toutes les catégories réussie."
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(
                    logService,
                    e,
                    "Erreur lors de la récupération de toutes les catégories."
            );
        }
    }

    // Récupération des catégories par leurs IDs
    @Override
    public ServiceResponse<CategoryDTO> getCategoryById(Long id) {
        try {
            Optional<Category> categoryOpt = categoryRepository.findById(id);

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

            Category category = categoryOpt.get();
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.CATEGORY_SUCCESS,
                    categoryMapper.toDTO(category).getData(),
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
    public ServiceResponse<CategoryDTO> getCategoryByName(String name) {
        try {
            Optional<Category> categoryOpt = categoryRepository.findByName(name);

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

            Category category = categoryOpt.get();
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.CATEGORY_SUCCESS,
                    categoryMapper.toDTO(category).getData(),
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
    public ServiceResponse<CategoryDTO> updateCategory(Long id, CategoryDTO categoryDTO) {
        try {
            Optional<Category> optional = categoryRepository.findById(id);

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

            Category existingCategory = optional.get();
            boolean nameAlreadyExists = !categoryDTO.getName().equals(existingCategory.getName()) &&
                    categoryRepository.existsByName(categoryDTO.getName());

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

            existingCategory.setName(categoryDTO.getName());
            Category updated = categoryRepository.save(existingCategory);

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.CATEGORY_UPDATED,
                    categoryMapper.toDTO(updated).getData(),
                    false,
                    "Catégorie mise à jour : " + updated.getName()
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(
                    logService,
                    e,
                    "Erreur lors de la mise à jour de la catégorie avec ID " + id
            );
        }
    }

    // Création d'une catégorie
    @Transactional
    @Override
    public ServiceResponse<CategoryDTO> createCategory(CategoryDTO categoryDTO) {
        try {
            if (categoryRepository.existsByName(categoryDTO.getName())) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.CATEGORY_ALREADY_EXISTS,
                        null,
                        true,
                        "La catégorie avec le nom '{}' existe déjà.",
                        categoryDTO.getName()
                );
            }
            
            Category category = categoryMapper.toEntity(categoryDTO).getData();

            Category savedCategory = categoryRepository.save(category);

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.CATEGORY_CREATED,
                    categoryMapper.toDTO(savedCategory).getData(),
                    false,
                    "Catégorie créée avec succès : {}",
                    savedCategory.getName()
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(
                    logService,
                    e,
                    "Erreur lors de la création de la catégorie avec le nom '{}'",
                    categoryDTO.getName()
            );
        }
    }

    // Suppression d'une catégorie
    @Transactional
    @Override
    public ServiceResponse<Void> deleteCategory(Long id) {
        try {
            if (!categoryRepository.existsById(id)) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.CATEGORY_NOT_FOUND,
                        null,
                        true,
                        "Échec suppression : Catégorie non trouvée avec ID " + id
                );
            }

            categoryRepository.deleteById(id);
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.CATEGORY_DELETED,
                    null,
                    false,
                    "Catégorie supprimée avec ID " + id
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(
                    logService,
                    e,
                    "Erreur lors de la suppression de la catégorie avec ID " + id
            );
        }
    }
}
