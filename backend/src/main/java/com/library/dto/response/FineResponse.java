package com.library.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder
public class FineResponse {
    private Long id;
    private Long borrowRecordId;
    private String bookTitle;
    private String memberName;
    private BigDecimal amount;
    private boolean paid;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
}
