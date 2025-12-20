package com.drop.shiping.api.drop_shiping_api.transactions.entities;

import com.drop.shiping.api.drop_shiping_api.products.entities.Product;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "product_items")
public class ProductItem {
    @Id
    @UuidGenerator
    @Column(name = "id",  nullable = false, updatable = false)
    private String id;

    @ManyToOne
    @JoinColumn
    private Transaction transaction;

    @ManyToOne
    private Product product;
    private Integer quantity;

    public ProductItem() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
