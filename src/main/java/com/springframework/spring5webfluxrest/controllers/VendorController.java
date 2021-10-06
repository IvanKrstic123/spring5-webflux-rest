package com.springframework.spring5webfluxrest.controllers;

import com.springframework.spring5webfluxrest.domain.Category;
import com.springframework.spring5webfluxrest.domain.Vendor;
import com.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class VendorController {

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping("/api/v1/vendors")
    Flux<Vendor> getVendors() {
        return vendorRepository.findAll();
    }

    @GetMapping("/api/v1/vendors/{id}")
    Mono<Vendor> getVendorById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/vendors/")
    Mono<Void> createVendor(@RequestBody Publisher<Vendor> vendorStream){
        return vendorRepository.saveAll(vendorStream).then();
    }

    @PutMapping("/api/v1/vendors/{id}")
    Mono<Vendor> updateVendor(@PathVariable String id,
                              @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/api/v1/vendors/{id}")
    Mono<Vendor> updateCategoryPatch(@PathVariable String id,
                                       @RequestBody Vendor vendor) {

        return vendorRepository.findById(id)
                .flatMap(vendor1 -> {
                    // implement logic for all props
                    if (!vendor1.getFirstname().equals(vendor.getFirstname())) {
                        vendor1.setFirstname(vendor.getFirstname());
                        vendor1.setLastname(vendor.getLastname());

                        return vendorRepository.save(vendor1);
                    }
                    return Mono.just(vendor);
                });
    }

}
