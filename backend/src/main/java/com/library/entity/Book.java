package com.library.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String isbn;

    @Column(nullable = false, length = 255)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "available_qty", nullable = false)
    private int availableQty;

    @Column(name = "published_year")
    private Integer publishedYear;

    @Column(length = 255)
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String description;
}
