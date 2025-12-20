package com.drop.shiping.api.drop_shiping_api.transactions.entities;

import com.drop.shiping.api.drop_shiping_api.users.entities.User;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Transaction {
    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private String id;
    private String reference;
    private Date transactionDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "transaction")
    private List<ProductItem> products;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
    private String userReference;

    public Transaction() {}

    public Transaction(String orderName, String notes, Date orderDate) {
        this.reference = orderName;
        this.transactionDate = orderDate;

    }

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getReference() {
        return reference;
    }
    
    public void setReference(String orderName) {
        this.reference = orderName;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date orderDate) {
        this.transactionDate = orderDate;
    }

    public List<ProductItem> getProducts() {
        return products;
    }

    public void setProducts(List<ProductItem> products) {
        this.products = products;
    }

    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }

    public String getUserReference() {
        return userReference;
    }

    public void setUserReference(String userReference) {
        this.userReference = userReference;
    }
}
