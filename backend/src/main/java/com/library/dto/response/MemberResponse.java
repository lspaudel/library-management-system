package com.library.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder
public class MemberResponse {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDate membershipExpiry;
    private LocalDateTime createdAt;
}
