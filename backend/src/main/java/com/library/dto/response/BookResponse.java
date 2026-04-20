package com.library.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class BookResponse {
    private Long id;
    private String isbn;
    private String title;
    private AuthorResponse author;
    private CategoryResponse category;
    private int quantity;
    private int availableQty;
    private Integer publishedYear;
    private String imageUrl;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
