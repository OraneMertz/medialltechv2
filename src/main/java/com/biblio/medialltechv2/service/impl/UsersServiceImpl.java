package com.biblio.medialltechv2.service.impl;

import com.biblio.medialltechv2.model.Users;
import com.biblio.medialltechv2.repository.RoleRepository;
import com.biblio.medialltechv2.repository.UsersRepository;
import com.biblio.medialltechv2.service.UsersService;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Profile("prod")
@Service
public class UsersServiceImpl implements UsersService {
    private final RoleRepository roleRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Users> getUsers() {
        return usersRepository.findAll();
    }

    @Override
    public Users getUserById(String id) {
        return usersRepository.findById(id).orElse(null);
    }

    @Override
    public Users getUserByUsername(String username) {
        return usersRepository.getUserByUsername(username);
    }

    @Override
    public void addUser(Users user) {
        if (!user.isPasswordChanged()) {
            usersRepository.save(user);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setPasswordChanged(true);
            usersRepository.save(user);
        }
    }

    @Override
    public void updateUser(Users user) {
        usersRepository.save(user);
    }

    @Override
    public void deleteUserById(String id) {
        usersRepository.deleteById(id);
    }
}
