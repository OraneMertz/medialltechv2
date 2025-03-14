package com.biblio.medialltechv2.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BorrowersDTO {
    private String id;
    private String username;
    private String email;
    private boolean isActive;
    private List<BooksDTO> borrowedBooksIds;

    public BorrowersDTO(String id, String username, String email, boolean isActive, List<BooksDTO> borrowedBooksIds) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.isActive = isActive;
        this.borrowedBooksIds = borrowedBooksIds;
    }
}
