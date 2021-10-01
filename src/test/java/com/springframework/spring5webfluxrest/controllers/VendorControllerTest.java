package com.springframework.spring5webfluxrest.controllers;

import com.springframework.spring5webfluxrest.domain.Vendor;
import com.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
}