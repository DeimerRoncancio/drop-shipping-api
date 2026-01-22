package com.drop.shiping.api.drop_shiping_api.products.specification;

import com.drop.shiping.api.drop_shiping_api.products.entities.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> hasProductName(String productName) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("productName")), "%" + productName + "%");
    }

    public static Specification<Product> hasCategory(String category) {
        return (root, query, cb) ->
                cb.equal(root.get("categories").get("categoryName"), category);
    }
}
