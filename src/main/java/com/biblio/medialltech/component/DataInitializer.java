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
        if (roleRepository.count() == 0) {
            Role userRole = new Role("ROLE_USER");
            Role adminRole = new Role("ROLE_ADMIN");
            roleRepository.saveAll(List.of(userRole, adminRole));
            System.out.println("🔑 Rôles ajoutés : " + userRole + " & " + adminRole);
        }

        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(()-> new RuntimeException("ROLE_USER introuvable"));
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow(()-> new RuntimeException("ROLE_ADMIN introuvable"));

        System.out.println("🔎 Rôles récupérés : " + userRole + " & " + adminRole);

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

        if (categoryRepository.count() == 0) {
            List<Category> categories = List.of(
                    new Category(1L, "Informatique", new HashSet<>()),
                    new Category(2L, "Développement",  new HashSet<>()),
                    new Category(3L, "Communication",  new HashSet<>()),
                    new Category(4L, "Relationnel",  new HashSet<>())
            );
            categoryRepository.saveAll(categories);
            System.out.println("📚 Catégories ajoutées !");
        }

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

