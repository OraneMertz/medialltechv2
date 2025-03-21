package com.biblio.medialltech.service.impl;

import com.biblio.medialltech.dto.CategoryDTO;
import com.biblio.medialltech.entity.Category;
import com.biblio.medialltech.mapper.CategoryMapper;
import com.biblio.medialltech.repository.CategoryRepository;
import com.biblio.medialltech.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceJpaImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceJpaImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDTOList(categories);
    }

    @Override
    public Optional<CategoryDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::toDTO);
    }

    @Override
    public Optional<CategoryDTO> getCategoryByName(String name) {
        return categoryRepository.findByName(name).map(categoryMapper::toDTO);
    }

    @Transactional
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Le nom de la catégorie existe déjà.");
        }

        Category category = categoryMapper.toEntity(categoryDTO);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDTO(savedCategory);
    }

    @Transactional
    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (!existingCategory.isPresent()) {
            throw new IllegalArgumentException("Catégorie non trouvée avec l'id: " + id);
        }

        if (!categoryDTO.getName().equals(existingCategory.get().getName()) &&
                categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Une catégorie avec ce nom existe déjà.");
        }

        Category category = existingCategory.get();
        category.setName(categoryDTO.getName());

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toDTO(updatedCategory);
    }

    @Transactional
    @Override
    public boolean deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            return false;
        }
        categoryRepository.deleteById(id);
        return true;
    }
}
