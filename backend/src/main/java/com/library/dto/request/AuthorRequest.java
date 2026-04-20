package com.library.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AuthorRequest {
    @NotBlank @Size(min = 2, max = 150)
    private String name;

    @Size(max = 2000)
    private String bio;
}
