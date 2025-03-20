package com.biblio.medialltech.service.impl;

import com.biblio.medialltech.dto.UserDTO;
import com.biblio.medialltech.entity.User;
import com.biblio.medialltech.entity.Role;
import com.biblio.medialltech.repository.RoleRepository;
import com.biblio.medialltech.repository.UserRepository;
import com.biblio.medialltech.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceJpaImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceJpaImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> System.out.println(user.getUsername() + " → " + user.getRoleNames()));
        return users.stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getFullname(),
                        user.getEmail(),
                        user.getRoles())).collect(Collectors.toList());
        }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean authenticateUser(String username, String password) {
        Optional<User> userOptional = getUserByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return password.equals(user.getPassword());
        }
        return false;
    }

    @Override
    public User createUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Ce nom d'utilisateur existe déjà.");
        }

        Role defaultRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Le rôle ROLE_USER est introuvable."));

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(user.getPassword());
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of(defaultRole));
        }

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean isUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
