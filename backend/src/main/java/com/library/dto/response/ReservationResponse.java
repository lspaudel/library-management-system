package com.library.dto.response;

import com.library.entity.Reservation;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class ReservationResponse {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private Long memberId;
    private String memberName;
    private LocalDateTime reservedAt;
    private LocalDateTime expiresAt;
    private Reservation.ReservationStatus status;
}
