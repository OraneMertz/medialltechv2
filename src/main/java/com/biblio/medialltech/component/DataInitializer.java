package com.biblio.medialltech.component;

import com.biblio.medialltech.model.User;
import com.biblio.medialltech.repository.BookRepository;
import com.biblio.medialltech.repository.UserRepository;
import com.biblio.medialltech.model.Book;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public DataInitializer(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (bookRepository.count() == 0) {
            List<Book> books = List.of(
                    new Book(null, "Javascript pour les Nuls", "Eva Holland", "image", true, null),
                    new Book(null, "Coder proprement", "Robert C.Martin", "image", true, null)

            );
            bookRepository.saveAll(books);
            System.out.println("📚 Livres ajoutés !");
        }

        if (userRepository.count() == 0) {
            List<User> users = List.of(
                    new User(null, "admin", "Admin", "admin@exemple;fr", "admin123"),
                    new User(null, "user", "User1", "user1@example.com", "user123")
            );
            userRepository.saveAll(users);
            System.out.println("👤 Utilisateurs ajoutés !");
        }
    }
}

