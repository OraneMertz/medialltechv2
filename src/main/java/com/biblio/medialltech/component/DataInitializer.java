package com.biblio.medialltech.component;

import com.biblio.medialltech.dto.UserDTO;
import com.biblio.medialltech.entity.*;
import com.biblio.medialltech.repository.BookRepository;
import com.biblio.medialltech.repository.CategoryRepository;
import com.biblio.medialltech.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public DataInitializer(
            BookRepository bookRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            List<User> users = new ArrayList<>();

            UserDTO adminDTO = new UserDTO(null, "admin", "Admin", "admin@exemple.fr", Role.ADMIN);
            adminDTO.setPassword("admin123");
            users.add(convertToEntity(adminDTO));

            UserDTO userDTO = new UserDTO(null, "user", "User1", "user1@example.com", Role.USER);
            userDTO.setPassword("user123");
            users.add(convertToEntity(userDTO));

            userRepository.saveAll(users);
            System.out.println("👤 Utilisateurs ajoutés !");
        }

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

        if (bookRepository.count() == 0) {
            List<Book> books = new ArrayList<>();
            Category informatique = categoryRepository.findByName("Informatique").orElse(null);
            Category developpement = categoryRepository.findByName("Développement").orElse(null);

            Book book1 = new Book(null, "Javascript pour les Nuls", "Eva Holland", "image", BookStatus.AVAILABLE, null, informatique);
            Book book2 = new Book(null, "Coder proprement", "Robert C. Martin", "image", BookStatus.AVAILABLE, null, developpement);

            books.add(book1);
            books.add(book2);

            bookRepository.saveAll(books);
            System.out.println("📚 Livres ajoutés !");
        }
    }

    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setFullname(userDTO.getFullname());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setPassword(userDTO.getPassword());
        return user;
    }
}