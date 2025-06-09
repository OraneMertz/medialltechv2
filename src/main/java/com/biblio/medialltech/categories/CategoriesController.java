package com.biblio.medialltech.categories;

import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    // Récupérer toutes les catégories
    @GetMapping
    public ServiceResponse<List<CategoriesDTO>> getAllCategories() {
        return categoriesService.getAllCategories();
    }

    // Récupérer une catégorie par ID
    @GetMapping("/{id}")
    public ServiceResponse<CategoriesDTO> getCategoryById(@PathVariable Long id) {
        return categoriesService.getCategoryById(id);
    }

    // Récupérer une catégorie par nom
    @GetMapping("/name/{name}")
    public ServiceResponse<CategoriesDTO> getCategoryByName(@PathVariable String name) {
        return categoriesService.getCategoryByName(name);
    }

    // Créer une nouvelle catégorie
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse<CategoriesDTO> createCategory(@RequestBody CategoriesDTO categoriesDTO) {
        return categoriesService.createCategory(categoriesDTO);
    }

    // Mettre à jour une catégorie existante
    @PutMapping("/{id}")
    public ServiceResponse<CategoriesDTO> updateCategory(@PathVariable Long id, @RequestBody CategoriesDTO categoriesDTO) {
        return categoriesService.updateCategory(id, categoriesDTO);
    }

    // Supprimer une catégorie par ID
    @DeleteMapping("/{id}")
    public ServiceResponse<Void> deleteCategory(@PathVariable Long id) {
        return categoriesService.deleteCategory(id);
    }
}
