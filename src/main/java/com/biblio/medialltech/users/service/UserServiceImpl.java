package com.biblio.medialltech.users.service;

import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import com.biblio.medialltech.users.UserRepository;
import com.biblio.medialltech.users.dto.ChangePasswordDTO;
import com.biblio.medialltech.users.dto.UserDTO;
import com.biblio.medialltech.users.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final LogService logService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, LogService logService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.logService = logService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ServiceResponse<List<UserDTO>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();

            if (users.isEmpty()) {
                logService.info("Aucun utilisateur trouvé.");
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NO_CONTENT,
                        ResponseMessage.USER_NO_CONTENT,
                        null,
                        false,
                        "Aucun utilisateur trouvé"
                );
            }

            List<UserDTO> userDTOs = users.stream()
                    .map(userMapper::toDTO)
                    .toList();

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.USER_SUCCESS,
                    userDTOs,
                    false,
                    "Récupération de {} utilisateur(s) avec succès",
                    userDTOs.size()
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
    public ServiceResponse<UserDTO> getUserById(String id) {
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
    public ServiceResponse<Boolean> changePassword(String userId, ChangePasswordDTO changePasswordDTO) {
        try {
            // Vérification des paramètres d'entrée
            if (changePasswordDTO == null) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.INVALID_INPUT,
                        false,
                        true,
                        "Les données de changement de mot de passe sont nulles"
                );
            }

            String currentPassword = changePasswordDTO.getCurrentPassword();
            String newPassword = changePasswordDTO.getNewPassword();

            if (currentPassword == null || currentPassword.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.INVALID_INPUT,
                        false,
                        true,
                        "Le mot de passe actuel est requis"
                );
            }

            if (newPassword == null || newPassword.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.INVALID_INPUT,
                        false,
                        true,
                        "Le nouveau mot de passe est requis"
                );
            }

            // Récupération de l'utilisateur
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.NOT_FOUND,
                        ResponseMessage.USER_NOT_FOUND,
                        false,
                        true,
                        "Utilisateur non trouvé avec ID : {}",
                        userId
                );
            }

            User user = userOpt.get();

            // Vérification du mot de passe actuel
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.UNAUTHORIZED,
                        ResponseMessage.PASSWORD_NOT_MATCH,
                        false,
                        true,
                        "Mot de passe actuel incorrect pour l'utilisateur ID : {}",
                        userId
                );
            }

            // Chiffrement et mise à jour du nouveau mot de passe
            String encryptedNewPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encryptedNewPassword);
            userRepository.save(user);

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.USER_UPDATED,
                    true,
                    false,
                    "Mot de passe changé avec succès pour l'utilisateur : {}",
                    user.getPseudo()
            );

        } catch (Exception e) {
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.INTERNAL_ERROR,
                    ResponseMessage.USER_UPDATE_ERROR,
                    false,
                    true,
                    "Erreur lors du changement de mot de passe pour l'utilisateur ID : {}",
                    userId
            );
        }
    }

    @Override
    public ServiceResponse<UserDTO> createUser(UserDTO userDTO) {
        try {
            // Vérification des données d'entrée
            if (userDTO == null) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.USER_NULL,
                        null,
                        true,
                        "Les données utilisateur sont nulles"
                );
            }

            // Vérifier si l'utilisateur avec le même nom d'utilisateur existe déjà
            if (userRepository.existsByPseudo(userDTO.getPseudo())) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.ALREADY_EXISTS,
                        ResponseMessage.USERNAME_ALREADY_EXISTS,
                        null,
                        true,
                        "Le nom d'utilisateur '{}' existe déjà.",
                        userDTO.getPseudo()
                );
            }

            // Vérifier si l'utilisateur avec le même email existe déjà
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.ALREADY_EXISTS,
                        ResponseMessage.EMAIL_ALREADY_EXISTS,
                        null,
                        true,
                        "L'email '{}' existe déjà.",
                        userDTO.getEmail()
                );
            }

            User user = userMapper.toEntity(userDTO).getData();
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);

            User savedUser = userRepository.save(user);

            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.CREATED,
                    ResponseMessage.USER_CREATED,
                    userMapper.toDTO(savedUser),
                    false,
                    "Utilisateur créé avec succès : {}",
                    savedUser.getPseudo()
            );
        } catch (Exception e) {
            assert userDTO != null;
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.INTERNAL_ERROR,
                    ResponseMessage.USER_CREATION_ERROR,
                    null,
                    true,
                    "Erreur lors de la création de l'utilisateur avec le nom d'utilisateur '{}'",
                    userDTO.getPseudo()
            );
        }
    }

    @Override
    public ServiceResponse<UserDTO> updateUser(String id, UserDTO userDTO) {
        try {
            // Vérification des données d'entrée
            if (userDTO == null) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.BAD_REQUEST,
                        ResponseMessage.USER_NULL,
                        null,
                        true,
                        "Les données utilisateur sont nulles"
                );
            }

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

            // Vérification des doublons de nom d'utilisateur et d'email (si modifiés)
            if (userDTO.getPseudo() != null &&
                    !existingUser.getPseudo().equals(userDTO.getPseudo()) &&
                    userRepository.existsByPseudo(userDTO.getPseudo())) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.ALREADY_EXISTS,
                        ResponseMessage.USERNAME_ALREADY_EXISTS,
                        null,
                        true,
                        "Nom d'utilisateur '{}' existe déjà.",
                        userDTO.getPseudo()
                );
            }

            if (userDTO.getEmail() != null &&
                    !existingUser.getEmail().equals(userDTO.getEmail()) &&
                    userRepository.existsByEmail(userDTO.getEmail())) {
                return ServiceResponse.logAndRespond(
                        logService,
                        ResponseCode.ALREADY_EXISTS,
                        ResponseMessage.EMAIL_ALREADY_EXISTS,
                        null,
                        true,
                        "L'email '{}' existe déjà.",
                        userDTO.getEmail()
                );
            }

            // Utilisation de updateEntityFromDTO
            userMapper.updateEntityFromDTO(existingUser, userDTO);

            User updatedUser = userRepository.save(existingUser);
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    ResponseMessage.USER_UPDATED,
                    userMapper.toDTO(updatedUser),
                    false,
                    "Utilisateur mis à jour : {}",
                    updatedUser.getPseudo()
            );
        } catch (Exception e) {
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.INTERNAL_ERROR,
                    ResponseMessage.USER_UPDATE_ERROR,
                    null,
                    true,
                    "Erreur lors de la mise à jour de l'utilisateur avec ID '{}'",
                    id
            );
        }
    }

    @Override
    public ServiceResponse<Boolean> deleteUser(String id) {
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
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.INTERNAL_ERROR,
                    ResponseMessage.DELETE_USER_ERROR,
                    false,
                    true,
                    "Erreur lors de la suppression de l'utilisateur avec ID '{}'",
                    id
            );
        }
    }

    @Override
    public ServiceResponse<Boolean> isPseudoExists(String pseudo) {
        try {
            boolean exists = userRepository.existsByPseudo(pseudo);
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.SUCCESS,
                    exists ? ResponseMessage.USERNAME_NOT_AVAILABLE : ResponseMessage.USERNAME_AVAILABLE,
                    exists,
                    false,
                    "Vérification du nom d'utilisateur '{}' : {}",
                    pseudo, exists ? "non disponible" : "disponible"
            );
        } catch (Exception e) {
            return ServiceResponse.handleException(
                    logService,
                    e,
                    "Erreur lors de la vérification du nom d'utilisateur '{}'",
                    pseudo
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
                    email, exists ? "déjà utilisé" : "disponible"
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