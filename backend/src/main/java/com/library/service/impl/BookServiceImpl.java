package com.library.service.impl;

import com.library.dto.request.BookRequest;
import com.library.dto.response.AuthorResponse;
import com.library.dto.response.BookResponse;
import com.library.dto.response.CategoryResponse;
import com.library.entity.Author;
import com.library.entity.Book;
import com.library.entity.Category;
import com.library.exception.DuplicateResourceException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import com.library.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public Page<BookResponse> searchBooks(String q, Long categoryId, Long authorId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return bookRepository.searchBooks(q, categoryId, authorId, pageable).map(this::toResponse);
    }

    public BookResponse getById(Long id) {
        return toResponse(findBook(id));
    }

    public BookResponse create(BookRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateResourceException("Book", "ISBN", request.getIsbn());
        }
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author", request.getAuthorId()));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));

        Book book = Book.builder()
                .isbn(request.getIsbn()).title(request.getTitle())
                .author(author).category(category)
                .quantity(request.getQuantity()).availableQty(request.getQuantity())
                .publishedYear(request.getPublishedYear())
                .imageUrl(request.getImageUrl()).description(request.getDescription())
                .build();
        return toResponse(bookRepository.save(book));
    }

    public BookResponse update(Long id, BookRequest request) {
        Book book = findBook(id);
        if (!book.getIsbn().equals(request.getIsbn()) && bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateResourceException("Book", "ISBN", request.getIsbn());
        }
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author", request.getAuthorId()));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));

        int diff = request.getQuantity() - book.getQuantity();
        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setAuthor(author);
        book.setCategory(category);
        book.setQuantity(request.getQuantity());
        book.setAvailableQty(Math.max(0, book.getAvailableQty() + diff));
        book.setPublishedYear(request.getPublishedYear());
        book.setImageUrl(request.getImageUrl());
        book.setDescription(request.getDescription());
        return toResponse(bookRepository.save(book));
    }

    public void delete(Long id) {
        bookRepository.delete(findBook(id));
    }

    private Book findBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
    }

    public BookResponse toResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId()).isbn(book.getIsbn()).title(book.getTitle())
                .author(AuthorResponse.builder()
                        .id(book.getAuthor().getId()).name(book.getAuthor().getName()).build())
                .category(CategoryResponse.builder()
                        .id(book.getCategory().getId()).name(book.getCategory().getName()).build())
                .quantity(book.getQuantity()).availableQty(book.getAvailableQty())
                .publishedYear(book.getPublishedYear())
                .imageUrl(book.getImageUrl()).description(book.getDescription())
                .createdAt(book.getCreatedAt()).updatedAt(book.getUpdatedAt())
                .build();
    }
}
