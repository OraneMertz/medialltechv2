package com.biblio.medialltech.users;

import com.biblio.medialltech.logs.ServiceResponse;

import java.util.List;

public interface UserService {

    ServiceResponse<List<UserDTO>> getAllUsers();

    ServiceResponse<UserDTO> getUserById(Long id);

    ServiceResponse<UserDTO> getUserByUsername(String username);

    ServiceResponse<UserDTO> authenticateUser(String username, String password);

    ServiceResponse<UserDTO> createUser(UserDTO userDTO);

    ServiceResponse<UserDTO> updateUser(Long id, UserDTO userDTO);

    ServiceResponse<Boolean> deleteUser(Long id);

    ServiceResponse<Boolean> isUsernameExists(String username);

    ServiceResponse<Boolean> isEmailExists(String email);
}
