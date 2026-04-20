package com.library.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BookRequest {
    @NotBlank(message = "ISBN is required")
    @Size(min = 10, max = 20)
    private String isbn;

    @NotBlank(message = "Title is required")
    @Size(max = 255)
    private String title;

    @NotNull(message = "Author is required")
    private Long authorId;

    @NotNull(message = "Category is required")
    private Long categoryId;

    @NotNull @Min(1)
    private Integer quantity;

    @Min(1000) @Max(2100)
    private Integer publishedYear;

    private String imageUrl;

    @Size(max = 2000)
    private String description;
}
