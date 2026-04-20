package com.library.controller;

import com.library.dto.response.ApiResponse;
import com.library.dto.response.ReservationResponse;
import com.library.service.impl.ReservationServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations")
public class ReservationController {

    private final ReservationServiceImpl reservationService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponse>> reserve(
            @RequestParam Long bookId,
            @RequestParam Long memberId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Reservation created", reservationService.reserve(bookId, memberId)));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<ReservationResponse>> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Reservation cancelled", reservationService.cancel(id)));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<ApiResponse<Page<ReservationResponse>>> getByMember(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(reservationService.getByMember(memberId, page, size)));
    }
}
