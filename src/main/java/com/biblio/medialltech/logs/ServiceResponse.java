package com.biblio.medialltech.logs;

import javax.naming.AuthenticationException;
import java.sql.SQLException;

/**
 * Classe générique pour encapsuler les réponses des services
 * avec gestion des logs intégrée
 */
public class ServiceResponse<T> {

    private ResponseCode statusCode;
    private ResponseMessage message;
    private T data;
    private boolean error;
    private String logMessage;

    public ServiceResponse(ResponseCode statusCode, ResponseMessage message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public ServiceResponse() {
    }

    // Méthodes factory pour les réponses de succès
    public static <T> ServiceResponse<T> success(ResponseCode statusCode, ResponseMessage message, T data) {
        return new ServiceResponse<>(statusCode, message, data);
    }

    public static <T> ServiceResponse<T> successNoData(ResponseCode statusCode, ResponseMessage message) {
        return new ServiceResponse<>(statusCode, message, null);
    }

    // Méthodes factory pour les réponses d'erreur
    public static <T> ServiceResponse<T> error(ResponseCode statusCode, ResponseMessage message, T data) {
        return new ServiceResponse<>(statusCode, message, data);
    }

    public static <T> ServiceResponse<T> errorNoData(ResponseCode statusCode, ResponseMessage message) {
        return new ServiceResponse<>(statusCode, message, null);
    }

    public static <T> ServiceResponse<T> errorWithDefaultMessage(ResponseCode statusCode) {
        return new ServiceResponse<>(statusCode, ResponseMessage.UNEXPECTED_ERROR, null);
    }

    /**
     * Utilitaire centralisé pour enregistrer un message de log et renvoyer une réponse appropriée
     *
     * @param <T>        Type de données de la réponse
     * @param logService Service de log à utiliser
     * @param statusCode Code de réponse
     * @param message    Message de réponse
     * @param data       Données à renvoyer (peut être null)
     * @param error      Indique si c'est une erreur (détermine le niveau de log)
     * @param logMessage Message à logger
     * @param args       Arguments à inclure dans le message de log
     * @return ServiceResponse avec les paramètres spécifiés
     */
    public static <T> ServiceResponse<T> logAndRespond(LogService logService,
                                                       ResponseCode statusCode,
                                                       ResponseMessage message,
                                                       T data,
                                                       boolean error,
                                                       String logMessage,
                                                       Object... args) {
        logService.info(logMessage, args);

        ServiceResponse<T> response = new ServiceResponse<>();
        response.setStatusCode(statusCode);
        response.setMessage(message);
        response.setData(data);
        response.setError(error);
        return response;
    }

    public static <T> ServiceResponse<T> handleException(LogService logService, Exception e, String logMessage, Object... args) {
        logService.error(logMessage, args, e);

        ServiceResponse<T> response = new ServiceResponse<>();

        switch (e) {
            case SQLException ex:
                response.setStatusCode(ResponseCode.INTERNAL_ERROR);
                response.setMessage(ResponseMessage.DATABASE_ERROR);
                break;
            case NullPointerException ex:
                response.setStatusCode(ResponseCode.INTERNAL_ERROR);
                response.setMessage(ResponseMessage.GENERAL_ERROR);
                break;
            case AuthenticationException ex:
                response.setStatusCode(ResponseCode.UNAUTHORIZED);
                response.setMessage(ResponseMessage.AUTHENTICATION_FAILED);
                break;
            case IllegalArgumentException ex:
                response.setStatusCode(ResponseCode.BAD_REQUEST);
                response.setMessage(ResponseMessage.INVALID_INPUT);
                break;
            default:
                response.setStatusCode(ResponseCode.INTERNAL_ERROR);
                response.setMessage(ResponseMessage.UNEXPECTED_ERROR);
                break;
        }

        response.setData(null);
        response.setError(true);
        return response;
    }


    public ResponseCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(ResponseCode statusCode) {
        this.statusCode = statusCode;
    }

    public ResponseMessage getMessage() {
        return message;
    }

    public void setMessage(ResponseMessage message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }
}