package com.library.controller;

import com.library.dto.response.ApiResponse;
import com.library.dto.response.FineResponse;
import com.library.service.impl.FineServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
@Tag(name = "Fines")
public class FineController {

    private final FineServiceImpl fineService;

    @GetMapping("/member/{memberId}")
    public ResponseEntity<ApiResponse<Page<FineResponse>>> getByMember(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(fineService.getByMember(memberId, page, size)));
    }

    @PutMapping("/{id}/pay")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<FineResponse>> payFine(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Fine marked as paid", fineService.payFine(id)));
    }
}
