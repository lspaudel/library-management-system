package com.library.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank @Size(min = 2, max = 100)
    private String name;

    @Size(max = 500)
    private String description;
}
