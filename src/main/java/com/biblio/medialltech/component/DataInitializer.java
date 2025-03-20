package com.biblio.medialltech.component;

import com.biblio.medialltech.entity.Category;
import com.biblio.medialltech.entity.Role;
import com.biblio.medialltech.entity.User;
import com.biblio.medialltech.repository.BookRepository;
import com.biblio.medialltech.repository.CategoryRepository;
import com.biblio.medialltech.repository.RoleRepository;
import com.biblio.medialltech.repository.UserRepository;
import com.biblio.medialltech.entity.Book;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;

    public DataInitializer(
            BookRepository bookRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
        Role role = new Role(null, "ROLE_USER");
            return roleRepository.save(role);
        });

        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
        Role role = new Role(null, "ROLE_ADMIN");
            return roleRepository.save(role);
        });

        System.out.println("🔎 Rôles récupérés/créés : " + userRole + " & " + adminRole);

        // Vérifier et ajouter les utilisateurs
        if (userRepository.count() == 0) {
            List<User> users = List.of(
                    new User(null, "admin", "Admin", "admin@exemple.fr", "admin123",
                            new HashSet<>(Arrays.asList(userRole, adminRole))),
                    new User(null, "user", "User1", "user1@example.com", "user123",
                            new HashSet<>(Collections.singleton(userRole)))
            );
            userRepository.saveAll(users);
            System.out.println("👤 Utilisateurs ajoutés !");
        }

        // Vérifier et ajouter les catégories
        if (categoryRepository.count() == 0) {
            List<Category> categories = List.of(
                    new Category(null, "Informatique", new HashSet<>()),
                    new Category(null, "Développement", new HashSet<>()),
                    new Category(null, "Communication", new HashSet<>()),
                    new Category(null, "Relationnel", new HashSet<>())
            );
            categoryRepository.saveAll(categories);
            System.out.println("📚 Catégories ajoutées !");
        }

        // Vérifier et ajouter les livres
        if (bookRepository.count() == 0) {
            List<Book> books = List.of(
                    new Book(null, "Javascript pour les Nuls", "Eva Holland", "image", true, null),
                    new Book(null, "Coder proprement", "Robert C. Martin", "image", true, null)
            );
            bookRepository.saveAll(books);
            System.out.println("📚 Livres ajoutés !");
        }
    }
}
