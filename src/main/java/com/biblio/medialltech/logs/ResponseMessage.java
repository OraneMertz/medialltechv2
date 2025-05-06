package com.biblio.medialltech.logs;

public enum ResponseMessage {

    // Réponses de succès
    CATEGORY_SUCCESS("Catégorie(s) récupérée(s) avec succès."),
    BOOK_SUCCESS("Livre(s) récupéré(s) avec succès."),
    USER_SUCCESS("Utilisateur(s) récupéré(s) avec succès."),
    BORROWER_SUCCESS("Emprunteur(s) récupéré(s) avec succès."),
    BORROWING_SUCCESS("Emprunt effectué avec succès."),
    AUTHOR_SUCCESS("Livre(s) trouvé(s) pour l'auteur demandé : %s"),

    CATEGORY_DELETED("Catégorie(s) supprimée(s) avec succès."),
    BOOK_DELETED("Livre(s) supprimé(s) avec succès."),
    USER_DELETED("Utilisateur(s) supprimé(s) avec succès."),

    BOOK_RETURN_SUCCESS("Livre retourné avec succès."),
    BOOK_BORROW_SUCCESS("Livre emprunté avec succès."),
    BOOK_ALREADY_BORROWED("Le livre est déjà emprunté."),
    NO_AVAILABLE_BOOKS("Aucun livre disponible actuellement."),
    NO_BORROWINGS("Pas d'emprunteur."),
    NO_BOOKS_FOR_AUTHOR("Aucun livre trouvé pour l'auteur : "),

    CATEGORY_CREATED("Catégorie créée avec succès."),
    BOOK_CREATED("Livre créé avec succès."),
    USER_CREATED("Utilisateur créé avec succès."),

    CATEGORY_UPDATED("Catégorie mise à jour avec succès."),
    BOOK_UPDATED("Livre mis à jour avec succès."),
    USER_UPDATED("Utilisateur mis à jour avec succès."),

    CATEGORY_NO_CONTENT("Catégorie n'a pas de contenu."),
    BOOK_NO_CONTENT("Livre n'a pas de contenu."),
    USER_NO_CONTENT("Utilisateur n'a pas de contenu."),

    CATEGORY_NOT_FOUND("Catégorie non trouvée avec l'ID : %s"),
    BOOK_NOT_FOUND("Livre non trouvé avec l'ID : %s"),
    USER_NOT_FOUND("Utilisateur non trouvé avec l'ID : %s"),
    BORROWING_NOT_FOUND("Aucun emprunt correspondant à cette recherche."),
    BORROWING_MISSING_IDS("L'emprunteur n'a pas d'ID. %s"),
    
    CATEGORY_ALREADY_EXISTS("La catégorie existe déjà."),
    BOOK_ALREADY_EXISTS("Le livre existe déjà."),
    USER_ALREADY_EXISTS("L'utilisateur existe déjà."),
    USERNAME_ALREADY_EXISTS("Le nom d'utilisateur est déjà utilisé."),
    EMAIL_ALREADY_EXISTS("L'email est déjà utilisé."),

    BOOK_ALREADY_RETURNED("Livre déjà retourné."),

    CATEGORY_NULL("Liste de catégories vide."),
    BOOK_NULL("Le livre est null."),
    USER_NULL("L'utilisateur est null."),
    USER_ERROR("Erreur lors du mappage de l'utilisateur."),

    CATEGORY_BOOK_ERROR("La catégorie doit être associée à un livre."),
    BOOK_CATEGORY_ERROR("Le livre doit contenir au moins une catégorie."),

    INVALID_BOOK_DETAILS("Détails du livre fournis sont invalides."),
    INVALID_AUTHENTICATION("L'authentication est incorrect."),
    INVALID_FILE("Le fichier n'est pas valide."),

    CATEGORY_CREATION_ERROR("Erreur lors de la création de la catégorie : %s"),
    BOOK_CREATION_ERROR("Erreur lors de la création du livre : %s"),
    USER_CREATION_ERROR("Erreur lors de la création de l'utilisateur : %s"),

    CATEGORY_UPDATE_ERROR("Erreur lors de la mise à jour de la catégorie : %s"),
    BOOK_UPDATE_ERROR("Erreur lors de la mise à jour du livre : %s"),
    USER_UPDATE_ERROR("Erreur lors de la mise à jour de l'utilisateur : %s"),

    DELETE_CATEGORY_ERROR("Erreur lors de la suppression de la catégorie : %s"),
    DELETE_BOOK_ERROR("Erreur lors de la suppression du livre : %s"),
    DELETE_USER_ERROR("Erreur lors de la suppression de l'utilisateur : %s"),

    BOOK_NOT_BORROWED("Ce livre n'a pas été emprunté."),
    BORROWING_RETURNED("Livre retourné avec succès."),

    CATEGORY_ERROR("Catégorie non valide."),
    CATEGORY_INVALID("Catégorie invalide."),

    // Messages personnalisés pour la gestion des emprunteurs
    BORROWING_DTO_NULL("L'emprunteur ne peut pas être null.%s"),

    // Messages personnalisés pour la gestion des images
    IMAGE_UPDATE_ERROR("Erreur lors de la mise à jour de l'image pour le livre : %s"),
    IMAGE_UPLOAD_ERROR("Erreur lors de l'imporation de l'image %s"),
    IMAGE_DOWNLOAD_ERROR("Erreur lors du téléchargement de l'image sur Azure Blob Storage : %s"),
    IMAGE_DOWNLOAD_SUCCESS("Image téléchargée avec succès."),
    CONTAINER_NOT_FOUND("Conteneur Azure Blob Storage non trouvé."),

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
