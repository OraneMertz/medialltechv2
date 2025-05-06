package com.biblio.medialltech.users;

import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final LogService logService;

    public UserMapper(LogService logService) {
        this.logService = logService;
    }

    // Convertir un User en UserDTO
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    // Méthode pour mapper une liste de User en une liste de UserDTO
    public List<UserDTO> toDTOList(List<User> users) {
        if (users == null) {
            return null;
        }
        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Convertir un UserDTO en User
    public ServiceResponse<User> toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            logService.warn("Tentative de mappage d'un UserDTO null en User.");
            return ServiceResponse.error(ResponseCode.INVALID_DATA, ResponseMessage.USER_ERROR, null);
        }

        logService.info("Mappage de UserDTO vers l'entité User pour le DTO avec ID: {}", userDTO.getId());

        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setFullname(userDTO.getFullname());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());

        return ServiceResponse.success(ResponseCode.SUCCESS, ResponseMessage.USER_SUCCESS, user);
    }
}

