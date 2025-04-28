package com.biblio.medialltech.logs;

public enum ResponseCode {
    // Réponses de succès
    SUCCESS(200),
    CREATED(201),
    ACCEPTED(202),
    NO_CONTENT(204),

    // Messages de redirection
    NOT_MODIFIED(304),

    // Réponses d'erreur côté client
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    INVALID_FILE_TYPE(405),
    INVALID_DATA(406),
    TIME_OUT(408),
    ALREADY_EXISTS(409),

    // Réponses d'erreur côté serveur
    INTERNAL_ERROR(500),
    DATA_HANDLING_ERROR(501),
    BAD_GATEWAY(502),
    SERVICE_UNAVAILABLE(503),
    INSUFFICIENT_STORAGE(504),
    UNEXPECTED_ERROR(505);

    private final int code;

    ResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    // Permet de retrouver un ResponseCode à partir du code entier
    public static ResponseCode fromCode(int code) {
        for (ResponseCode rc : values()) {
            if (rc.code == code) return rc;
        }
        throw new IllegalArgumentException("Code inconnu : " + code);
    }
}
