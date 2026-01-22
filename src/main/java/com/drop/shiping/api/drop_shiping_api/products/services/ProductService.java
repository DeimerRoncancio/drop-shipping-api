package com.drop.shiping.api.drop_shiping_api.products.services;

import java.util.Optional;

import com.drop.shiping.api.drop_shiping_api.products.dtos.ProductDTO;
import com.drop.shiping.api.drop_shiping_api.products.dtos.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.drop.shiping.api.drop_shiping_api.products.entities.Product;

public interface ProductService {
    Page<ProductResponseDTO> findAll(Pageable pageable);

    Optional<ProductResponseDTO> findOne(String id);

    ProductDTO save(ProductDTO product);

    Optional<ProductDTO> update(String id, ProductDTO product);

    Optional<Product> delete(String id);

    Page<ProductResponseDTO> search(String query, String category, Pageable pageable);

    Long productsSize();
}
