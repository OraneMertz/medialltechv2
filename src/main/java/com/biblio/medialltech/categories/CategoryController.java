package com.biblio.medialltech.categories;

import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Récupérer toutes les catégories
    @GetMapping
    public ServiceResponse<List<CategoryDTO>> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // Récupérer une catégorie par ID
    @GetMapping("/{id}")
    public ServiceResponse<CategoryDTO> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    // Récupérer une catégorie par nom
    @GetMapping("/name/{name}")
    public ServiceResponse<CategoryDTO> getCategoryByName(@PathVariable String name) {
        return categoryService.getCategoryByName(name);
    }

    // Créer une nouvelle catégorie
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.createCategory(categoryDTO);
    }

    // Mettre à jour une catégorie existante
    @PutMapping("/{id}")
    public ServiceResponse<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(id, categoryDTO);
    }

    // Supprimer une catégorie par ID
    @DeleteMapping("/{id}")
    public ServiceResponse<Void> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }
}
