package com.library.controller;

import com.library.dto.request.BookRequest;
import com.library.dto.response.ApiResponse;
import com.library.dto.response.BookResponse;
import com.library.service.impl.BookServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Book management – CRUD and search")
public class BookController {

    private final BookServiceImpl bookService;

    @GetMapping
    @Operation(summary = "Search and list books with pagination")
    public ResponseEntity<ApiResponse<Page<BookResponse>>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(ApiResponse.success(bookService.searchBooks(q, categoryId, authorId, page, size, sortBy, sortDir)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID")
    public ResponseEntity<ApiResponse<BookResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(bookService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new book (ADMIN only)")
    public ResponseEntity<ApiResponse<BookResponse>> create(@Valid @RequestBody BookRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Book created", bookService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a book (ADMIN only)")
    public ResponseEntity<ApiResponse<BookResponse>> update(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Book updated", bookService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a book (ADMIN only)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Book deleted", null));
    }
}
