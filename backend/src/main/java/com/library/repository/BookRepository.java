package com.library.repository;

import com.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Optional<Book> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);

    @Query("SELECT b FROM Book b JOIN FETCH b.author JOIN FETCH b.category WHERE " +
           "(:q IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%',:q,'%')) OR LOWER(b.isbn) LIKE LOWER(CONCAT('%',:q,'%'))) AND " +
           "(:categoryId IS NULL OR b.category.id = :categoryId) AND " +
           "(:authorId IS NULL OR b.author.id = :authorId)")
    Page<Book> searchBooks(@Param("q") String q,
                           @Param("categoryId") Long categoryId,
                           @Param("authorId") Long authorId,
                           Pageable pageable);

    @Query("SELECT COUNT(b) FROM Book b WHERE b.category.id = :categoryId")
    long countByCategoryId(@Param("categoryId") Long categoryId);
}
