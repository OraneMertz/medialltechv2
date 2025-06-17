package com.biblio.medialltech.users;

import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

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
        dto.setUsername(user.getUsername());
        dto.setFullname(user.getFullname());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        return dto;
    }

    public ServiceResponse<User> toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            logService.warn("UserDTO est null.");
            return ServiceResponse.errorNoData(ResponseCode.BAD_REQUEST, ResponseMessage.USER_NULL);
        }

        // Validation des champs essentiels
        if (userDTO.getUsername() == null || userDTO.getUsername().trim().isEmpty()) {
            logService.warn("UserDTO invalide : username manquant.");
            return ServiceResponse.errorNoData(ResponseCode.BAD_REQUEST, ResponseMessage.INVALID_INPUT);
        }

        if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
            logService.warn("UserDTO invalide : email manquant.");
            return ServiceResponse.errorNoData(ResponseCode.BAD_REQUEST, ResponseMessage.INVALID_INPUT);
        }

        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername().trim());
        user.setFullname(userDTO.getFullname());
        user.setEmail(userDTO.getEmail().trim());
        user.setRole(userDTO.getRole());

        // Chiffrement automatique du password si fourni
        if (userDTO.getPassword() != null && !userDTO.getPassword().trim().isEmpty()) {
            String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(encryptedPassword);
            logService.info("Mot de passe chiffré pour l'utilisateur : {}", userDTO.getUsername());
        }

        logService.info("Mappage réussi de UserDTO à User avec username : {}", userDTO.getUsername());
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

        if (dto.getUsername() != null && !dto.getUsername().trim().isEmpty()) {
            user.setUsername(dto.getUsername().trim());
        }

        if (dto.getFullname() != null && !dto.getFullname().trim().isEmpty()) {
            user.setFullname(dto.getFullname().trim());
        }

        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            user.setEmail(dto.getEmail().trim());
        }

        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
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