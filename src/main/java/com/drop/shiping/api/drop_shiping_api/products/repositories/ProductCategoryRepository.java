package com.drop.shiping.api.drop_shiping_api.products.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.drop.shiping.api.drop_shiping_api.products.entities.ProductCategory;
import org.springframework.data.jpa.repository.Query;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String> {
    List<ProductCategory> findByCategoryNameIn(List<String> categories);
    List<ProductCategory> findTop3ByOrderByCreatedAtDesc();

    @Query("SELECT COUNT(DISTINCT c) FROM ProductCategory c LEFT JOIN c.products p WHERE p IS NULL")
    long countObsoleteCategories();
}
