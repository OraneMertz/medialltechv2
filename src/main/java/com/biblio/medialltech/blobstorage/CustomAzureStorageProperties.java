package com.biblio.medialltech.blobstorage;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "azure.storage")
@Validated
@Profile("test")
public class CustomAzureStorageProperties {
    
    @NotNull
    private String accountName;
    
    @NotNull
    private String accountKey;
    
    @NotNull
    private String containerName;
    
    @NotNull
    private String connectionString;
    
    public CustomAzureStorageProperties() {}

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getConnectionString() {
        return connectionString;
    }
    
    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }
}
