package com.biblio.medialltech.blobstorage;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.biblio.medialltech.logs.LogService;
import com.biblio.medialltech.logs.ResponseCode;
import com.biblio.medialltech.logs.ResponseMessage;
import com.biblio.medialltech.logs.ServiceResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Profile("test")
@Service
public class AzureBlobStorageService {

    private final LogService logService;
    private final BlobContainerClient blobContainerClient;

    public AzureBlobStorageService(CustomAzureStorageProperties azureStorageProperties,
                                   LogService logService) {
        this.logService = logService;

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(azureStorageProperties.getConnectionString())
                .buildClient();

        this.blobContainerClient = blobServiceClient.getBlobContainerClient(azureStorageProperties.getContainerName());
    }

    public ServiceResponse<String> uploadImage(MultipartFile file) {
        // Vérifier si le conteneur existe
        if (!blobContainerClient.exists()) {
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.NOT_FOUND,
                    ResponseMessage.CONTAINER_NOT_FOUND,
                    null,
                    false,
                    "Le conteneur '%s' n'existe pas dans Azure Blob Storage.",
                    blobContainerClient.getBlobContainerName()
            );
        }

        // Vérifier si le fichier est valide
        if (file == null || file.isEmpty()) {
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.BAD_REQUEST,
                    ResponseMessage.INVALID_FILE,
                    null,
                    false,
                    "Le fichier est vide ou null."
            );
        }

        // Vérifier si le fichier est une image
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.INVALID_FILE_TYPE,
                    ResponseMessage.INVALID_FILE,
                    null,
                    false,
                    "Le fichier '%s' n'est pas une image.",
                    file.getOriginalFilename()
            );
        }

        // Créer un nom de fichier unique
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);

        try (InputStream inputStream = file.getInputStream()) {
            logService.info("Téléchargement de l'image '{}' en cours...", fileName);
            blobClient.upload(inputStream, file.getSize(), true);
        } catch (IOException e) {
            return ServiceResponse.logAndRespond(
                    logService,
                    ResponseCode.INTERNAL_ERROR,
                    ResponseMessage.INTERNAL_ERROR,
                    null,
                    false,
                    "Erreur lors du téléchargement de l'image '%s' : %s",
                    file.getOriginalFilename(),
                    e.getMessage()
            );
        }

        // Retourner l'URL de l'image
        String imageUrl = blobClient.getBlobUrl();
        return ServiceResponse.logAndRespond(
                logService,
                ResponseCode.SUCCESS,
                ResponseMessage.IMAGE_DOWNLOAD_SUCCESS,
                imageUrl,
                false,
                "Image '%s' téléchargée avec succès.",
                fileName
        );
    }
}
