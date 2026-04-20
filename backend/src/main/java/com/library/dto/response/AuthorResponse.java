package com.library.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class AuthorResponse {
    private Long id;
    private String name;
    private String bio;
    private int bookCount;
    private LocalDateTime createdAt;
}
