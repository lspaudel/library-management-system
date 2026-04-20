package com.library.controller;

import com.library.dto.request.BorrowRequest;
import com.library.dto.response.ApiResponse;
import com.library.dto.response.BorrowResponse;
import com.library.service.impl.BorrowServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
@RequiredArgsConstructor
@Tag(name = "Borrow", description = "Borrow and return books")
public class BorrowController {

    private final BorrowServiceImpl borrowService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BorrowResponse>> borrow(@Valid @RequestBody BorrowRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Book borrowed successfully", borrowService.borrow(request)));
    }

    @PutMapping("/{id}/return")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BorrowResponse>> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Book returned", borrowService.returnBook(id)));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<ApiResponse<Page<BorrowResponse>>> getByMember(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(borrowService.getByMember(memberId, page, size)));
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<BorrowResponse>>> getOverdue() {
        return ResponseEntity.ok(ApiResponse.success(borrowService.getOverdue()));
    }
}
