package com.library.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BorrowRequest {
    @NotNull Long bookId;
    @NotNull Long memberId;
    @NotNull @Min(1) @Max(30)
    Integer durationDays;
}
