package com.library.controller;

import com.library.dto.request.MemberRequest;
import com.library.dto.response.ApiResponse;
import com.library.dto.response.MemberResponse;
import com.library.service.impl.MemberServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Members")
public class MemberController {

    private final MemberServiceImpl memberService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<MemberResponse>>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(memberService.search(q, page, size)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MemberResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(memberService.getById(id)));
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<ApiResponse<MemberResponse>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(memberService.getByUserId(userId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponse>> update(@PathVariable Long id, @Valid @RequestBody MemberRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Member updated", memberService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Member deleted", null));
    }
}
