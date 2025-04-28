package com.biblio.medialltech.users;

import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceJpaImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final LogService logService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceJpaImpl(UserRepository userRepository, UserMapper userMapper, LogService logService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.logService = logService;
    }

    @Override
    public ServiceResponse<List<UserDTO>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            logService.info("Récupération de tous les utilisateurs : " + users.size() + " trouvés.");
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.USER_SUCCESS,
                    userMapper.toDTOList(users),
                    false,
                    "Récupération de tous les utilisateurs réussie"
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(
                    logService,
                    e,
                    "Erreur lors de la récupération des utilisateurs"
            );
        }
    }

    @Override
    public ServiceResponse<UserDTO> getUserById(Long id) {
        try {
            Optional<User> userOpt = userRepository.findById(id);
            return userOpt.map(user -> ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.USER_SUCCESS,
                    userMapper.toDTO(user),
                    false,
                    "Utilisateur trouvé avec ID : " + id
            )).orElseGet(() -> ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.NOT_FOUND,
                    ResponseMessage.USER_NOT_FOUND,
                    null,
                    true,
                    "Utilisateur non trouvé avec ID : " + id
            ));
        } catch (Exception e) {
            return ServiceResponse.handleException(
                    logService,
                    e,
                    "Erreur lors de la récupération de l'utilisateur avec l'ID : " + id
            );
        }
    }

    @Override
    public ServiceResponse<UserDTO> getUserByUsername(String username) {
        try {
            Optional<User> userOpt = userRepository.findByUsername(username);
            return userOpt.map(user -> ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.USER_SUCCESS,
                    userMapper.toDTO(user),
                    false,
                    "Utilisateur trouvé avec le nom d'utilisateur : " + username
            )).orElseGet(() -> ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.NOT_FOUND,
                    ResponseMessage.USER_NOT_FOUND,
                    null,
                    true,
                    "Utilisateur non trouvé avec le nom d'utilisateur : " + username
            ));
        } catch (Exception e) {
            return ServiceResponse.handleException(
                    logService,
                    e,
                    "Erreur lors de la récupération de l'utilisateur avec le nom d'utilisateur : " + username
            );
        }
    }

    @Override
    public ServiceResponse<UserDTO> authenticateUser(String username, String password) {
        try {
            ServiceResponse<UserDTO> response = getUserByUsername(username);

            // Vérifie si l'utilisateur existe
            if (response.getData() == null) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.USER_NOT_FOUND,
                        null,
                        true,
                        "Utilisateur non trouvé avec le nom : {}",
                        username
                );
            }

            UserDTO userDTO = response.getData();
            Optional<User> userEntityOpt = userRepository.findByUsername(username);

            if (userEntityOpt.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.USER_NOT_FOUND,
                        null,
                        true,
                        "Utilisateur non trouvé dans la base : {}",
                        username
                );
            }

            User userEntity = userEntityOpt.get();

            // Vérifie le mot de passe avec l'encoder
            if (!passwordEncoder.matches(password, userEntity.getPassword())) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.UNAUTHORIZED,
                        ResponseMessage.AUTHENTICATION_FAILED,
                        null,
                        true,
                        "Mot de passe incorrect pour l'utilisateur : {}",
                        username
                );
            }

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.AUTHENTICATION_SUCCESS,
                    userDTO,
                    false,
                    "Authentification réussie pour l'utilisateur : {}",
                    username
            );

        } catch (Exception e) {
            return ServiceResponse.handleException(
                    logService,
                    e,
                    "Erreur lors de l'authentification de l'utilisateur : {}",
                    username
            );
        }
    }


    @Override
    public ServiceResponse<UserDTO> createUser(UserDTO userDTO) {
        try {

            if (userRepository.existsByUsername(userDTO.getUsername())) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.USERNAME_ALREADY_EXISTS,
                        null,
                        true,
                        "Le nom d'utilisateur '{}' existe déjà.",
                        userDTO.getUsername()
                );
            }

            // Vérifier si l'utilisateur avec le même email existe déjà
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.EMAIL_ALREADY_EXISTS,
                        null,
                        true,
                        "L'email '{}' existe déjà.",
                        userDTO.getEmail()
                );
            }
            
            // Encrypter le mot de passe
            String rawPassword = userDTO.getPassword();
            String encryptedPassword = passwordEncoder.encode(rawPassword);
            userDTO.setPassword(encryptedPassword);

            User user = userMapper.toEntity(userDTO).getData();
            User savedUser = userRepository.save(user);

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.USER_CREATED,
                    userMapper.toDTO(savedUser),
                    false,
                    "Utilisateur créé avec succès : {}",
                    savedUser.getUsername()
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(
                    logService,
                    e,
                    "Erreur lors de la création de l'utilisateur avec le nom d'utilisateur '{}'",
                    userDTO.getUsername()
            );
        }
    }

    @Override
    public ServiceResponse<UserDTO> updateUser(Long id, UserDTO userDTO) {
        try {
            Optional<User> userOpt = userRepository.findById(id);

            if (userOpt.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.USER_NOT_FOUND,
                        null,
                        true,
                        "Utilisateur non trouvé avec ID : " + id
                );
            }

            User existingUser = userOpt.get();

            // Vérification des doublons de nom d'utilisateur et d'email
            if (!existingUser.getUsername().equals(userDTO.getUsername()) &&
                    userRepository.existsByUsername(userDTO.getUsername())) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.USERNAME_ALREADY_EXISTS,
                        null,
                        true,
                        "Nom d'utilisateur '{}' existe déjà.",
                        userDTO.getUsername()
                );
            }

            if (!existingUser.getEmail().equals(userDTO.getEmail()) &&
                    userRepository.existsByEmail(userDTO.getEmail())) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.EMAIL_ALREADY_EXISTS,
                        null,
                        true,
                        "L'email '{}' existe déjà.",
                        userDTO.getEmail()
                );
            }

            // Mettre à jour l'utilisateur
            existingUser.setUsername(userDTO.getUsername());
            existingUser.setFullname(userDTO.getFullname());
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setRole(userDTO.getRole());

            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                existingUser.setPassword(userDTO.getPassword());
            }

            User updatedUser = userRepository.save(existingUser);
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.USER_UPDATED,
                    userMapper.toDTO(updatedUser),
                    false,
                    "Utilisateur mis à jour : {}",
                    updatedUser.getUsername()
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(
                    logService,
                    e,
                    "Erreur lors de la mise à jour de l'utilisateur avec ID '{}'",
                    id
            );
        }
    }

    @Override
    public ServiceResponse<Boolean> deleteUser(Long id) {
        try {
            if (!userRepository.existsById(id)) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.USER_NOT_FOUND,
                        false,
                        true,
                        "Utilisateur non trouvé avec ID : " + id
                );
            }

            userRepository.deleteById(id);
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.USER_DELETED,
                    true,
                    false,
                    "Utilisateur supprimé avec ID : " + id
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(
                    logService,
                    e,
                    "Erreur lors de la suppression de l'utilisateur avec ID '{}'",
                    id
            );
        }
    }

    @Override
    public ServiceResponse<Boolean> isUsernameExists(String username) {
        try {
            boolean exists = userRepository.existsByUsername(username);
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    exists ? ResponseMessage.USERNAME_ALREADY_EXISTS : ResponseMessage.USERNAME_AVAILABLE,
                    exists,
                    false,
                    "Vérification du nom d'utilisateur '{}' : {}",
                    username, exists
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(
                    logService,
                    e,
                    "Erreur lors de la vérification du nom d'utilisateur '{}'",
                    username
            );
        }
    }

    @Override
    public ServiceResponse<Boolean> isEmailExists(String email) {
        try {
            boolean exists = userRepository.existsByEmail(email);
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    exists ? ResponseMessage.EMAIL_ALREADY_EXISTS : ResponseMessage.EMAIL_AVAILABLE,
                    exists,
                    false,
                    "Vérification de l'email '{}' : {}",
                    email, exists
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(
                    logService,
                    e,
                    "Erreur lors de la vérification de l'email '{}'",
                    email
            );
        }
    }
}
