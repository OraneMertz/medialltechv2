package com.biblio.medialltech.logs;

public enum ResponseMessage {

    // Réponses de succès
    CATEGORY_SUCCESS("Catégorie(s) récupérée(s) avec succès."),
    BOOK_SUCCESS("Livre(s) récupéré(s) avec succès."),
    USER_SUCCESS("Utilisateur(s) récupéré(s) avec succès."),
    BORROWER_SUCCESS("Emprunteur(s) récupéré(s) avec succès."),
    BORROWING_SUCCESS("Emprunt effectué avec succès."),
    BOOK_RETURN_SUCCESS("Livre retourné avec succès."),
    BOOK_BORROW_SUCCESS("Livre emprunté avec succès."),
    AUTHOR_SUCCESS("Livre(s) trouvé(s) pour l'auteur demandé : %s"),

    // Réponses pour les suppressions
    CATEGORY_DELETED("Catégorie(s) supprimée(s) avec succès."),
    BOOK_DELETED("Livre(s) supprimé(s) avec succès."),
    USER_DELETED("Utilisateur(s) supprimé(s) avec succès."),
    BORROWER_DELETED("Emprunteur(s) supprimé(s) avec succès."),

    // Réponses personnalisées pour les livres
    BOOK_ALREADY_BORROWED("Le livre est déjà emprunté."),
    BOOK_ALREADY_RETURNED("Livre déjà retourné."),

    // Réponses de création
    CATEGORY_CREATED("Catégorie créée avec succès."),
    BOOK_CREATED("Livre créé avec succès."),
    USER_CREATED("Utilisateur créé avec succès."),
    BORROWER_CREATED("Emprunteur créé avec succès."),

    // Réponses de mise à jour
    CATEGORY_UPDATED("Catégorie mise à jour avec succès."),
    BOOK_UPDATED("Livre mis à jour avec succès."),
    USER_UPDATED("Utilisateur mis à jour avec succès."),

    // Réponses pour "Pas de contenu"
    CATEGORY_NO_CONTENT("Catégorie n'a pas de contenu."),
    BOOK_NO_CONTENT("Livre n'a pas de contenu."),
    USER_NO_CONTENT("Utilisateur n'a pas de contenu."),

    // Réponses pour non trouvés et vides
    CATEGORY_NOT_FOUND("Catégorie non trouvée avec l'ID : %s"),
    BOOK_NOT_FOUND("Livre non trouvé avec l'ID : %s"),
    USER_NOT_FOUND("Utilisateur non trouvé avec l'ID : %s"),
    BORROWING_NOT_FOUND("Aucun emprunt correspondant à cette recherche."),
    BORROWING_MISSING_IDS("L'emprunteur n'a pas d'ID. %s"),
    BORROWER_NOT_FOUND("Aucun emprunteur trouvé avec l'ID %s."),
    NO_AVAILABLE_BOOKS("Aucun livre disponible actuellement."),
    NO_BORROWINGS("Pas d'emprunteur."),
    NO_BORROWED_BOOKS("Aucun livre emprunté trouvé."),
    NO_BOOKS_FOR_AUTHOR("Aucun livre trouvé pour l'auteur : %s"),
    NO_BOOKS_FOR_BORROWER("Aucun livre trouvé pour l'emprunteur : %s"),
    NO_BORROWER_FOR_USERNAME("Aucun emprunteur trouvé pour le nom d'utilisateur : %s"),
    NO_BORROWER_FOR_BOOK("Aucun emprunteur trouvé pour le livre avec l'ID : %s"),
    NO_BORROWER_FOR_CATEGORY("Aucun emprunteur trouvé pour la catégorie : %s"),

    // Réponses pour les existants
    CATEGORY_ALREADY_EXISTS("La catégorie existe déjà."),
    BOOK_ALREADY_EXISTS("Le livre existe déjà."),
    USER_ALREADY_EXISTS("L'utilisateur existe déjà."),
    USERNAME_ALREADY_EXISTS("Le nom d'utilisateur est déjà utilisé."),
    EMAIL_ALREADY_EXISTS("L'email est déjà utilisé."),

    // Réponses pour les NULL
    CATEGORY_NULL("Liste de catégories vide."),
    BOOK_NULL("Le livre est null."),
    USER_NULL("L'utilisateur est null."),

    CATEGORY_BOOK_ERROR("La catégorie doit être associée à un livre."),
    BOOK_CATEGORY_ERROR("Le livre doit contenir au moins une catégorie."),
    USER_ERROR("Erreur lors du mappage de l'utilisateur."),
    BORROWER_ERROR("Erreur lors de la récupération des informations de l'emprunteur : %s"),
    CATEGORY_CREATION_ERROR("Erreur lors de la création de la catégorie : %s"),
    BOOK_CREATION_ERROR("Erreur lors de la création du livre : %s"),
    USER_CREATION_ERROR("Erreur lors de la création de l'utilisateur : %s"),
    CATEGORY_UPDATE_ERROR("Erreur lors de la mise à jour de la catégorie : %s"),
    BOOK_UPDATE_ERROR("Erreur lors de la mise à jour du livre : %s"),
    USER_UPDATE_ERROR("Erreur lors de la mise à jour de l'utilisateur : %s"),
    DELETE_CATEGORY_ERROR("Erreur lors de la suppression de la catégorie : %s"),
    DELETE_BOOK_ERROR("Erreur lors de la suppression du livre : %s"),
    DELETE_USER_ERROR("Erreur lors de la suppression de l'utilisateur : %s"),

    INVALID_BOOK_DETAILS("Détails du livre fournis sont invalides."),
    INVALID_AUTHENTICATION("L'authentication est incorrect."),
    INVALID_FILE("Le fichier n'est pas valide."),
    INVALID_STATUS("Status invalide pour le lvire : %s"),

    BOOK_NOT_BORROWED("Ce livre n'a pas été emprunté."),
    BORROWING_RETURNED("Livre retourné avec succès."),

    CATEGORY_INVALID("Catégorie invalide."),
    BORROWING_DTO_NULL("L'emprunteur ne peut pas être null.%s"),

    // Messages personnalisés pour la gestion des images
    IMAGE_SUCCESS("L'image a été mise à jour. avec succès."),
    IMAGE_DOWNLOAD_SUCCESS("Image téléchargée avec succès."),
    IMAGE_UPDATE_ERROR("Erreur lors de la mise à jour de l'image pour le livre : %s"),
    IMAGE_UPLOAD_ERROR("Erreur lors de l'imporation de l'image %s"),
    IMAGE_DOWNLOAD_ERROR("Erreur lors du téléchargement de l'image : %s"),
    IMAGE_URL_NOT_FOUND("L'URL de n'a pas été trouvé."),
    IMAGE_URL_EMPTY("L'URL de l'image n'est pas valide."),

    // Messages personnalisés pour les erreurs
    INTERNAL_ERROR("Une erreur interne du serveur est survenue"),
    DATABASE_ERROR("Problème de communication avec la base de données."),
    CONFIG_ERROR("Erreur de configuration de l'application."),
    DATA_HANDLING_ERROR("Erreur lors du traitement des données."),
    UNEXPECTED_ERROR("Erreur inattendue"),
    SERVICE_UNAVAILABLE("Le service est temporairement indisponible."),
    INVALID_INPUT("Entrée invalide"),
    GENERAL_ERROR("Erreur générale"),
    ACCESS_DENIED("Accès refusé"),

    AUTHENTICATION_FAILED("Échec de l'authentification"),
    AUTHENTICATION_SUCCESS("Authentification réussie."),
    USERNAME_AVAILABLE("Le nom d'utilisateur est disponbile."),
    USERNAME_NOT_AVAILABLE("Le nom d'utilisateur n'est pas disponible."),
    EMAIL_AVAILABLE("L'email est disponible."),

    NOT_FOUND("Ressource non trouvée"),
    SERVER_UNAVAILABLE("Serveur non disponible"),
    TIMEOUT_ERROR("Erreur de délai d'attente"),
    VALIDATION_ERROR("Erreur de validation"),
    DUPLICATE_ENTRY("Entrée en double");

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    // Permet de formater le message avec des arguments
    public String format(Object... args) {
        return String.format(this.message, args);
    }
}
