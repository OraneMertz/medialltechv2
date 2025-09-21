package com.biblio.medialltech.categories;

import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final LogService logService;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryMapper categoryMapper,
                               LogService logService) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.logService = logService;
    }

    public List<CategoryDTO> getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll();

            List<CategoryDTO> categoryDTOS = categories.stream()
                    .map(categoryMapper::toDTO)
                    .toList();

            logService.info("Récupération de {} catégorie(s) avec succès", categoryDTOS.size());
            return categoryDTOS;

        } catch (Exception e) {
            ServiceResponse.handleException(logService, e, "Erreur lors de la récupération de toutes les catégories");
            return new ArrayList<>();
        }
    }

    @Override
    public CategoryDTO getCategoryById(String id) {
        try {
            Optional<Category> categoryOpt = categoryRepository.findById(id);

            if (categoryOpt.isEmpty()) {
                logService.warn("Catégorie non trouvée avec ID {}", id);
                return null;
            }

            Category category = categoryOpt.get();
            logService.info("Catégorie trouvée avec ID {}", id);
            return categoryMapper.toDTO(category);

        } catch (Exception e) {
            ServiceResponse.handleException(logService, e, "Erreur lors de la récupération de la catégorie avec ID {}", id);
            return null;
        }
    }

    @Override
    public CategoryDTO getCategoryByName(String name) {
        try {
            Optional<Category> categoryOpt = categoryRepository.findByName(name);

            if (categoryOpt.isEmpty()) {
                logService.warn("Catégorie non trouvée avec le nom {}", name);
                return null;
            }

            Category category = categoryOpt.get();
            logService.info("Catégorie trouvée avec le nom {}", name);
            return categoryMapper.toDTO(category);

        } catch (Exception e) {
            ServiceResponse.handleException(logService, e, "Erreur lors de la récupération de la catégorie avec le nom {}", name);
            return null;
        }
    }

    @Transactional
    @Override
    public CategoryDTO updateCategory(String id, CategoryDTO categoryDTO) {
        try {
            // Vérification des données d'entrée
            if (categoryDTO == null) {
                logService.warn("Les données de catégorie sont nulles");
                return null;
            }

            Optional<Category> optional = categoryRepository.findById(id);

            if (optional.isEmpty()) {
                logService.warn("Échec mise à jour : ID non trouvé {}", id);
                return null;
            }

            Category existingCategory = optional.get();
            boolean nameAlreadyExists = !categoryDTO.getName().equals(existingCategory.getName()) &&
                    categoryRepository.existsByName(categoryDTO.getName());

            if (nameAlreadyExists) {
                logService.warn("Échec mise à jour : nom de catégorie déjà existant.");
                return null;
            }

            existingCategory.setName(categoryDTO.getName());
            Category updated = categoryRepository.save(existingCategory);

            logService.info("Catégorie mise à jour : {}", updated.getName());
            return categoryMapper.toDTO(updated);

        } catch (Exception e) {
            ServiceResponse.handleException(logService, e, "Erreur lors de la mise à jour de la catégorie avec ID {}", id);
            return null;
        }
    }

    @Transactional
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        try {
            // Vérification des données d'entrée
            if (categoryDTO == null) {
                logService.warn("Les données de catégorie sont nulles");
                return null;
            }

            if (categoryRepository.existsByName(categoryDTO.getName())) {
                logService.warn("La catégorie avec le nom '{}' existe déjà.", categoryDTO.getName());
                return null;
            }

            Category category = categoryMapper.toEntity(categoryDTO).getData();
            Category savedCategory = categoryRepository.save(category);

            logService.info("Catégorie créée avec succès : {}", savedCategory.getName());
            return categoryMapper.toDTO(savedCategory);

        } catch (Exception e) {
            ServiceResponse.handleException(logService, e, "Erreur lors de la création de la catégorie avec le nom '{}'",
                    categoryDTO != null ? categoryDTO.getName() : "null");
            return null;
        }
    }

    @Override
    public String deleteCategory(String id) {
        try {
            if (!categoryRepository.existsById(id)) {
                logService.warn("Échec suppression : Catégorie non trouvée avec ID {}", id);
                return null;
            }

            categoryRepository.deleteById(id);
            logService.info("Catégorie supprimée avec ID {}", id);
            return id;

        } catch (Exception e) {
            ServiceResponse.handleException(logService, e, "Erreur lors de la suppression de la catégorie avec ID {}", id);
            return null;
        }
    }
}