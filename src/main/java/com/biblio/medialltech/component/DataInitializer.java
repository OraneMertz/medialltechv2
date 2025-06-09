package com.biblio.medialltech.component;

import com.biblio.medialltech.books.Book;
import com.biblio.medialltech.categories.Categories;
import com.biblio.medialltech.users.Role;
import com.biblio.medialltech.users.User;
import com.biblio.medialltech.users.UserDTO;
import com.biblio.medialltech.books.BookRepository;
import com.biblio.medialltech.categories.CategoriesRepository;
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
    private final CategoriesRepository categoriesRepository;

    public DataInitializer(BookRepository bookRepository,
                           UserRepository userRepository,
                           CategoriesRepository categoriesRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.categoriesRepository = categoriesRepository;
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

            UserDTO adminDTO = new UserDTO(null, "admin", "Admin", "admin@example.fr", "admin123", Role.ADMIN);
            adminDTO.setPassword("admin123");

            UserDTO userDTO = new UserDTO(null, "user", "User1", "user1@example.com", "admin123", Role.USER);
            userDTO.setPassword("user123");

            users.add(convertToEntity(adminDTO));
            users.add(convertToEntity(userDTO));

            userRepository.saveAll(users);
            System.out.println("👤 Utilisateurs ajoutés !");
        }
    }

    private void initCategories() {
        if (categoriesRepository.count() == 0) {
            List<Categories> categories = List.of(
                    new Categories(null, "Informatique", new ArrayList<>()),
                    new Categories(null, "Développement", new ArrayList<>()),
                    new Categories(null, "Communication", new ArrayList<>()),
                    new Categories(null, "Relationnel", new ArrayList<>())
            );
            categoriesRepository.saveAll(categories);
            System.out.println("📚 Catégories ajoutées !");
        }
    }

    private void initBooks() {
        if (bookRepository.count() == 0) {
            Categories informatique = categoriesRepository.findByName("Informatique").orElse(null);
            Categories developpement = categoriesRepository.findByName("Développement").orElse(null);

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