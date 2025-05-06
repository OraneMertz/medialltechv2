package com.biblio.medialltech.component;

import com.biblio.medialltech.books.Book;
import com.biblio.medialltech.categories.Category;
import com.biblio.medialltech.users.Role;
import com.biblio.medialltech.users.User;
import com.biblio.medialltech.users.UserDTO;
import com.biblio.medialltech.books.BookRepository;
import com.biblio.medialltech.categories.CategoryRepository;
import com.biblio.medialltech.users.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.biblio.medialltech.books.BookStatus.AVAILABLE;

@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public DataInitializer(BookRepository bookRepository,
                           UserRepository userRepository,
                           CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override   
    public void run(String... args) {
        initUsers();
        initCategories();
        initBooks();
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            List<User> users = new ArrayList<>();

            UserDTO adminDTO = new UserDTO(null, "admin", "Admin", "admin@example.fr", Role.ADMIN);
            adminDTO.setPassword("admin123");

            UserDTO userDTO = new UserDTO(null, "user", "User1", "user1@example.com", Role.USER);
            userDTO.setPassword("user123");

            users.add(convertToEntity(adminDTO));
            users.add(convertToEntity(userDTO));

            userRepository.saveAll(users);
            System.out.println("👤 Utilisateurs ajoutés !");
        }
    }

    private void initCategories() {
        if (categoryRepository.count() == 0) {
            List<Category> categories = List.of(
                    new Category(null, "Informatique", new ArrayList<>()),
                    new Category(null, "Développement", new ArrayList<>()),
                    new Category(null, "Communication", new ArrayList<>()),
                    new Category(null, "Relationnel", new ArrayList<>())
            );
            categoryRepository.saveAll(categories);
            System.out.println("📚 Catégories ajoutées !");
        }
    }

    private void initBooks() {
        if (bookRepository.count() == 0) {
            Category informatique = categoryRepository.findByName("Informatique").orElse(null);
            Category developpement = categoryRepository.findByName("Développement").orElse(null);

            if (informatique == null || developpement == null) {
                System.err.println("❌ Erreur : certaines catégories sont manquantes !");
                return;
            }

            List<Book> books = List.of(
                    new Book("Javascript pour les Nuls", "Eva Holland", "image", AVAILABLE, "admin"),
                    new Book( "Coder proprement", "Robert C. Martin", "image", AVAILABLE, "user")
            );

            bookRepository.saveAll(books);
            System.out.println("📚 Livres ajoutés !");
        }
    }

    private User convertToEntity(UserDTO userDTO) {
        return new User(
                userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getFullname(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getRole()
        );
    }
}