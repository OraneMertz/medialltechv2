package com.biblio.medialltech.blobstorage;

import com.biblio.medialltech.repository.BookRepository;
import com.biblio.medialltech.service.AzureBlobStorageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class AzureBlobStorageController {
    
    private final AzureBlobStorageService azureBlobStorageService;
    private final BookRepository bookRepository;
    
    public AzureBlobStorageController(AzureBlobStorageService azureBlobStorageService, BookRepository bookRepository) {
        this.azureBlobStorageService = azureBlobStorageService;
        this.bookRepository = bookRepository;
    }


}
