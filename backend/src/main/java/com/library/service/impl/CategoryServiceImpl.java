package com.library.service.impl;

import com.library.dto.request.CategoryRequest;
import com.library.dto.response.CategoryResponse;
import com.library.entity.Category;
import com.library.exception.DuplicateResourceException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll(org.springframework.data.domain.Sort.by("name"))
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getById(Long id) {
        return toResponse(findCategory(id));
    }

    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Category", "name", request.getName());
        }
        Category c = Category.builder().name(request.getName()).description(request.getDescription()).build();
        return toResponse(categoryRepository.save(c));
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category c = findCategory(id);
        c.setName(request.getName());
        c.setDescription(request.getDescription());
        return toResponse(categoryRepository.save(c));
    }

    public void delete(Long id) {
        categoryRepository.delete(findCategory(id));
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }

    private CategoryResponse toResponse(Category c) {
        return CategoryResponse.builder()
                .id(c.getId()).name(c.getName()).description(c.getDescription())
                .bookCount(bookRepository.countByCategoryId(c.getId()))
                .build();
    }
}
