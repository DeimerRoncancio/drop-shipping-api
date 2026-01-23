package com.drop.shiping.api.drop_shiping_api.products.controllers;

import com.drop.shiping.api.drop_shiping_api.common.exceptions.NotFoundException;
import com.drop.shiping.api.drop_shiping_api.products.dtos.ProductDTO;
import com.drop.shiping.api.drop_shiping_api.products.dtos.ProductResponseDTO;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.drop.shiping.api.drop_shiping_api.products.entities.Product;
import com.drop.shiping.api.drop_shiping_api.products.services.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/app/products")
@CrossOrigin(originPatterns = "*")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public Page<ProductResponseDTO> viewAll(@PageableDefault Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> view(@PathVariable("id") String id) {
        Optional<ProductResponseDTO> productDb = service.findOne(id);
        ProductResponseDTO product = productDb.orElseThrow(() -> new NotFoundException("Product not found"));

        return ResponseEntity.ok().body(product);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> create(@ModelAttribute @Valid ProductDTO product) {
        ProductDTO newProduct = service.save(product);
        return ResponseEntity.ok().body(newProduct);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> update(@ModelAttribute @Valid ProductDTO productDTO,
    @PathVariable("id") String id) {
        Optional<ProductDTO> productDb = service.update(id, productDTO);
        ProductDTO product = productDb.orElseThrow(() -> new NotFoundException("Product not found"));

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        Optional<Product> productDb = service.delete(id);
        productDb.orElseThrow(() -> new NotFoundException("Product not found"));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/query")
    public Page<ProductResponseDTO> findByName(
    @PageableDefault Pageable pageable, @RequestParam(value = "name", required = false) String name,
    @RequestParam(value = "categories", required = false) List<String> categories) {
        return service.search(name, categories, pageable);
    }
}
