package com.biblio.medialltech.users.service;

import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import com.biblio.medialltech.users.dto.UserDTO;
import com.biblio.medialltech.users.entity.User;
import com.biblio.medialltech.users.entity.UserBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final LogService logService;
    private final PasswordEncoder passwordEncoder;

    public UserMapper(LogService logService, PasswordEncoder passwordEncoder) {
        this.logService = logService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO toDTO(User user) {
        if (user == null) {
            logService.warn("Tentative de mapping d'un User nul vers UserDTO.");
            return null;
        }

        UserDTO dto = new UserDTO();

        dto.setId(user.getId());
        dto.setPseudo(user.getPseudo());
        dto.setEmail(user.getEmail());
        dto.setAuthorities(user.getAuthorities());
        dto.setAccountNonExpired(user.getAccountNonExpired());
        dto.setAccountNonLocked(user.getAccountNonLocked());
        dto.setCredentialsNonExpired(user.getCredentialsNonExpired());

        return dto;
    }

    public ServiceResponse<User> toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            logService.warn("UserDTO est null.");
            return ServiceResponse.errorNoData(ResponseCode.BAD_REQUEST, ResponseMessage.USER_NULL);
        }

        // Validation des champs essentiels
        if (userDTO.getPseudo() == null || userDTO.getPseudo().trim().isEmpty()) {
            logService.warn("UserDTO invalide : pseudo manquant.");
            return ServiceResponse.errorNoData(ResponseCode.BAD_REQUEST, ResponseMessage.INVALID_INPUT);
        }

        if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
            logService.warn("UserDTO invalide : email manquant.");
            return ServiceResponse.errorNoData(ResponseCode.BAD_REQUEST, ResponseMessage.INVALID_INPUT);
        }

        User user = new UserBuilder()
                .withId(userDTO.getId())
                .withPseudo(userDTO.getPseudo().trim())
                .withEmail(userDTO.getEmail().trim())
                .withAuthorities(userDTO.getAuthorities())
                .withAccountNonExpired(userDTO.getAccountNonExpired())
                .withAccountNonLocked(userDTO.getAccountNonLocked())
                .withCredentialsNonExpired(userDTO.getCredentialsNonExpired())
                .createUser();

        // Chiffrement automatique du password si fourni
        if (userDTO.getPassword() != null && !userDTO.getPassword().trim().isEmpty()) {
            String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(encryptedPassword);
            logService.info("Mot de passe chiffré pour l'utilisateur : {}", userDTO.getPseudo());
        }

        logService.info("Mappage réussi de UserDTO à User avec pseudo : {}", userDTO.getPseudo());
        return ServiceResponse.success(ResponseCode.SUCCESS, ResponseMessage.USER_SUCCESS, user);
    }

    /**
     * Méthode pour mettre à jour un User existant avec les données du DTO
     */
    public void updateEntityFromDTO(User user, UserDTO dto) {
        if (user == null || dto == null) {
            logService.warn("Impossible de mettre à jour : user ou dto est null");
            return;
        }

        if (dto.getPseudo() != null && !dto.getPseudo().trim().isEmpty()) {
            user.setPseudo(dto.getPseudo().trim());
        }

        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            user.setEmail(dto.getEmail().trim());
        }

        if (dto.getAuthorities() != null) {
            user.setAuthorities(dto.getAuthorities());
        }

        if (dto.getAccountNonExpired() != null) {
            user.setAccountNonExpired(dto.getAccountNonExpired());
        }

        if (dto.getAccountNonLocked() != null) {
            user.setAccountNonLocked(dto.getAccountNonLocked());
        }

        if (dto.getCredentialsNonExpired() != null) {
            user.setCredentialsNonExpired(dto.getCredentialsNonExpired());
        }

        // Gestion du password avec chiffrement
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            String encryptedPassword = passwordEncoder.encode(dto.getPassword());
            user.setPassword(encryptedPassword);
            logService.info("Mot de passe mis à jour et chiffré pour l'utilisateur ID : {}", user.getId());
        }

        logService.info("Mise à jour de l'entité User avec ID : {}", user.getId());
    }
}