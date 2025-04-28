package com.biblio.medialltech.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.biblio.medialltech.blobstorage.CustomAzureStorageProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class AzureBlobStorageService {

    private final CustomAzureStorageProperties azureStorageProperties;

    public AzureBlobStorageService(CustomAzureStorageProperties azureStorageProperties) {
        this.azureStorageProperties = azureStorageProperties;
    }

    public String uploadImage(MultipartFile file) {
        String containerName = azureStorageProperties.getContainerName();
        String connectionString = azureStorageProperties.getConnectionString();

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);

        // Vérifier si le conteneur existe
        if (!blobContainerClient.exists()) {
            throw new IllegalStateException("Le conteneur n'existe pas dans Azure Storage.");
        }

        // Générer un nom de fichier unique
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);

        // Vérifier que le fichier est bien une image
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Le fichier doit être une image.");
        }

        // Télécharger l'image sur Azure Blob Storage
        try (InputStream inputStream = file.getInputStream()) {
            blobClient.upload(inputStream, file.getSize(), true);
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("Erreur lors de l'upload sur Azure Blob Storage", e);
        }

        // Retourner l'URL du fichier téléchargé
        return blobClient.getBlobUrl();
    }
}

