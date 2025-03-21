package com.biblio.medialltech.mapper;

import com.biblio.medialltech.dto.BorrowingDTO;
import com.biblio.medialltech.entity.Borrowing;
import org.springframework.stereotype.Component;

@Component
public class BorrowingMapper {

    public BorrowingDTO toDTO(Borrowing borrowing) {
        if (borrowing == null) {
            return null;
        }
        return new BorrowingDTO(
                borrowing.getId(),
                borrowing.getBook().getId(),
                borrowing.getUser().getId(),
                borrowing.getBorrowDate(),
                borrowing.getReturnDate()
        );
    }

    public Borrowing toEntity(BorrowingDTO borrowingDTO) {
        if (borrowingDTO == null) {
            return null;
        }
        Borrowing borrowing = new Borrowing();
        borrowing.setId(borrowingDTO.getId());
        borrowing.setBorrowDate(borrowingDTO.getBorrowDate());
        borrowing.setReturnDate(borrowingDTO.getReturnDate());
        return borrowing;
    }
}
