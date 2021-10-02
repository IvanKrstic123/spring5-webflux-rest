package com.springframework.spring5webfluxrest.controllers;

import com.springframework.spring5webfluxrest.domain.Category;
import com.springframework.spring5webfluxrest.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryRepository categoryRepository;
    CategoryController categoryController;

    @BeforeEach
    void setUp() {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();

    }

    @Test
    void getCategories() {
        BDDMockito.given(categoryRepository.findAll())
                .willReturn(Flux.just(Category.builder().description("Meat").build(),
                        Category.builder().description("Egg").build()));

        webTestClient.get()
                .uri("/api/v1/categories/")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategoryById() {
        BDDMockito.given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("Mear").build()));

        webTestClient.get()
                .uri("/api/v1/categories/3")
                .exchange()
                .expectBody(Category.class);

        verify(categoryRepository, times(1)).findById(anyString());
    }

    @Test
    void createCategoryTest() {
        BDDMockito.given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().description("Meat").build()));

        Mono<Category> categoryToSave = Mono.just(Category.builder().description("build category").build());

        webTestClient.post()
                .uri("/api/v1/categories/")
                .body(categoryToSave, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void testUpdateCategory () {
        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToSaveMono = Mono.just(Category.builder().description("Juices").build());

        webTestClient.put()
                .uri("/api/v1/categories/asdasdsad")
                .body(catToSaveMono, Category.class)
                .exchange()
                .expectStatus().isOk();
    }
}