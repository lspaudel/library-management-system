package com.library.controller;

import com.library.dto.request.AuthorRequest;
import com.library.dto.response.ApiResponse;
import com.library.dto.response.AuthorResponse;
import com.library.service.impl.AuthorServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@Tag(name = "Authors")
public class AuthorController {

    private final AuthorServiceImpl authorService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuthorResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(authorService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthorResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(authorService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AuthorResponse>> create(@Valid @RequestBody AuthorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Author created", authorService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AuthorResponse>> update(@PathVariable Long id, @Valid @RequestBody AuthorRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Author updated", authorService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Author deleted", null));
    }
}
