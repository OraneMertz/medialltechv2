package com.biblio.medialltech.mapper;

import com.biblio.medialltech.dto.UserDTO;
import com.biblio.medialltech.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getFullname(),
                user.getEmail(),
                user.getRole()
        );
    }
}
