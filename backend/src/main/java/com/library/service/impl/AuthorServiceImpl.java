package com.library.service.impl;

import com.library.dto.request.AuthorRequest;
import com.library.dto.response.AuthorResponse;
import com.library.entity.Author;
import com.library.exception.DuplicateResourceException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public List<AuthorResponse> getAll() {
        return authorRepository.findAllOrderByName().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AuthorResponse getById(Long id) {
        return toResponse(findAuthor(id));
    }

    public AuthorResponse create(AuthorRequest request) {
        if (authorRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Author", "name", request.getName());
        }
        Author author = Author.builder().name(request.getName()).bio(request.getBio()).build();
        return toResponse(authorRepository.save(author));
    }

    public AuthorResponse update(Long id, AuthorRequest request) {
        Author author = findAuthor(id);
        author.setName(request.getName());
        author.setBio(request.getBio());
        return toResponse(authorRepository.save(author));
    }

    public void delete(Long id) {
        authorRepository.delete(findAuthor(id));
    }

    private Author findAuthor(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
    }

    private AuthorResponse toResponse(Author a) {
        return AuthorResponse.builder()
                .id(a.getId()).name(a.getName()).bio(a.getBio())
                .bookCount((int) bookRepository.count())
                .createdAt(a.getCreatedAt()).build();
    }
}
