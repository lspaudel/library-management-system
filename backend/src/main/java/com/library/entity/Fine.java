package com.library.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fines")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Fine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrow_record_id", nullable = false, unique = true)
    private BorrowRecord borrowRecord;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    @Builder.Default
    private boolean paid = false;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;
}
