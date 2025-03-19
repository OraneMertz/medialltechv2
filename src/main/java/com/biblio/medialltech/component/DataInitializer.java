package com.biblio.medialltech.component;

import com.biblio.medialltech.model.Role;
import com.biblio.medialltech.model.User;
import com.biblio.medialltech.repository.BookRepository;
import com.biblio.medialltech.repository.RoleRepository;
import com.biblio.medialltech.repository.UserRepository;
import com.biblio.medialltech.model.Book;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public DataInitializer(BookRepository bookRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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

