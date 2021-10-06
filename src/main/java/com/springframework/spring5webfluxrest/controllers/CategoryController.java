package com.springframework.spring5webfluxrest.controllers;

import com.springframework.spring5webfluxrest.domain.Category;
import com.springframework.spring5webfluxrest.repositories.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/api/v1/categories/")
    Flux<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/api/v1/categories/{id}")
    Mono<Category> getCategoryById(@PathVariable String id) {
        return categoryRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/categories/")
    Mono<Void> createCategory(@RequestBody Publisher<Category> categoryStream) {
        return categoryRepository.saveAll(categoryStream).then();
    }

    @PutMapping("/api/v1/categories/{id}")
    Mono<Category> updateCategory(@PathVariable String id,
                                  @RequestBody Category category) {
        category.setId(id);
        return categoryRepository.save(category);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/api/v1/categories/{id}")
    Mono<Category> updateCategoryPath(@PathVariable String id,
                                      @RequestBody Category category) {

        return categoryRepository.findById(id).
                flatMap(category1 -> {
                    if (category1.getDescription() != category.getDescription()){
                        category1.setDescription(category.getDescription());
                        return categoryRepository.save(category1);
                    }
                    return Mono.just(category1);
                });
    }
}
