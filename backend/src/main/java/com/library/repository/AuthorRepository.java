package com.library.repository;

import com.library.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT a FROM Author a ORDER BY a.name")
    List<Author> findAllOrderByName();
}
