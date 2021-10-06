package com.springframework.spring5webfluxrest.controllers;

import com.springframework.spring5webfluxrest.domain.Category;
import com.springframework.spring5webfluxrest.domain.Vendor;
import com.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @BeforeEach
    void setUp() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void getVendors() {
        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstname("Ivan").lastname("Krstic").build(),
                        Vendor.builder().firstname("Stojan").lastname("Stojanovic").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);

        verify(vendorRepository, times(1)).findAll();

    }

    @Test
    void getVendorById() {

        BDDMockito.given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstname("Ivan").lastname("Krstic").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/somesid")
                .exchange()
                .expectBody(Vendor.class);

        verify(vendorRepository, times(1)).findById(anyString());
    }

    @Test
    void createVendorTest() {
        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().description("Meat").build()));

        Mono<Category> vendorToSaveMono = Mono.just(Category.builder().description("build category").build());

        webTestClient.post()
                .uri("/api/v1/vendors/")
                .body(vendorToSaveMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void testUdateVendor () {
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorMonoSave = Mono.just(Vendor.builder().firstname("Ivan").lastname("Krstic").build());

        webTestClient.put()
                .uri("/api/v1/vendors/asdasdsad")
                .body(vendorMonoSave, Vendor.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testPatchVendorWithChanges() {
        BDDMockito.given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstname("Ivan").lastname("Krstic").build()));

        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstname("Ivana").lastname("Krstic").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/asdasdsad")
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus().isOk();

        verify(vendorRepository, times(1)).save(any(Vendor.class));
    }

    @Test
    void testPatchVendorNoChanges() {
        BDDMockito.given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstname("Ivan").build()));

        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstname("Ivan").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/asdasdsad")
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus().isOk();

        verify(vendorRepository, never()).save(any(Vendor.class));
    }
}