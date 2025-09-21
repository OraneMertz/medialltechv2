package com.biblio.medialltech.users.service;

import com.biblio.medialltech.logs.ServiceResponse;
import com.biblio.medialltech.users.dto.ChangePasswordDTO;
import com.biblio.medialltech.users.dto.UserDTO;

import java.util.List;

public interface UserService {

    ServiceResponse<List<UserDTO>> getAllUsers();

    ServiceResponse<UserDTO> getUserById(String id);

    ServiceResponse<Boolean> changePassword(String userId, ChangePasswordDTO changePasswordDTO);

    ServiceResponse<UserDTO> createUser(UserDTO userDTO);

    ServiceResponse<UserDTO> updateUser(String id, UserDTO userDTO);

    ServiceResponse<Boolean> deleteUser(String id);

    ServiceResponse<Boolean> isEmailExists(String email);

    ServiceResponse<Boolean> isPseudoExists(String username);
}