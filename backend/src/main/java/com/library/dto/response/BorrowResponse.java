package com.library.dto.response;

import com.library.entity.BorrowRecord;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data @Builder
public class BorrowResponse {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private String bookIsbn;
    private Long memberId;
    private String memberName;
    private LocalDateTime borrowedAt;
    private LocalDateTime dueDate;
    private LocalDateTime returnedAt;
    private BorrowRecord.BorrowStatus status;
    private BigDecimal fineAmount;
    private boolean finePaid;
}
