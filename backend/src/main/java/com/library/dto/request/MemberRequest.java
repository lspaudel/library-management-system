package com.library.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class MemberRequest {
    @NotBlank @Size(min = 2, max = 100)
    private String name;

    @Size(max = 20)
    private String phone;

    @Size(max = 255)
    private String address;

    private LocalDate membershipExpiry;
}
