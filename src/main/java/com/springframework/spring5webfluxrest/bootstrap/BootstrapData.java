package com.springframework.spring5webfluxrest.bootstrap;

import com.springframework.spring5webfluxrest.domain.Category;
import com.springframework.spring5webfluxrest.domain.Vendor;
import com.springframework.spring5webfluxrest.repositories.CategoryRepository;
import com.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData implements CommandLineRunner {

    private final VendorRepository vendorRepository;
    private final CategoryRepository categoryRepository;

    public BootstrapData(VendorRepository vendorRepository, CategoryRepository categoryRepository) {
        this.vendorRepository = vendorRepository;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        if (categoryRepository.count().block() == 0) {
            categoryRepository.save(Category.builder()
                    .description("Fruits").build()).block();

            categoryRepository.save(Category.builder()
                    .description("Nuts")
                    .build()).block();

            categoryRepository.save(Category.builder()
                    .description("Breads")
                    .build()).block();

            categoryRepository.save(Category.builder()
                    .description("Meats")
                    .build()).block();

            vendorRepository.save(Vendor.builder()
                    .firstname("Ivan")
                    .lastname("Krstic").build()).block();

            vendorRepository.save(Vendor.builder()
                    .firstname("Stanislav")
                    .lastname("Stanisavljevic").build()).block();

            vendorRepository.save(Vendor.builder()
                    .firstname("Zvonko")
                    .lastname("Zvonimirovic").build()).block();

            vendorRepository.save(Vendor.builder()
                    .firstname("Stojko")
                    .lastname("Stojkovic").build()).block();

            System.out.println("Category Data loaded: " + categoryRepository.count().block());
            System.out.println("Vendor Data loaded : " + vendorRepository.count().block());
        }
    }
}
