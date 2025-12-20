package com.drop.shiping.api.drop_shiping_api.transactions.controllers;

import com.drop.shiping.api.drop_shiping_api.transactions.dtos.OrderResponseDTO;
import jakarta.validation.Valid;
import com.drop.shiping.api.drop_shiping_api.common.exceptions.NotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drop.shiping.api.drop_shiping_api.transactions.entities.Transaction;
import com.drop.shiping.api.drop_shiping_api.transactions.dtos.NewOrderDTO;
import com.drop.shiping.api.drop_shiping_api.transactions.dtos.UpdateOrderDTO;
import com.drop.shiping.api.drop_shiping_api.transactions.services.TransactionService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

@RestController
@RequestMapping("/app/orders")
@CrossOrigin(originPatterns = "*")
public class TransactionController {
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Page<OrderResponseDTO> viewAll(@PageableDefault Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderResponseDTO> view(@PathVariable String id) {
        Optional<OrderResponseDTO> orderDb = service.findOne(id);

        return orderDb.map(order -> ResponseEntity.ok().body(order))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<NewOrderDTO> create(@Valid @RequestBody NewOrderDTO order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(order));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UpdateOrderDTO> update(@Valid @RequestBody UpdateOrderDTO order,
    @PathVariable String id) {
        Optional<Transaction> orderDb = service.update(id, order);
        orderDb.orElseThrow(() -> new NotFoundException("Order not found"));

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        Optional<Transaction> orderDb = service.delete(id);
        orderDb.orElseThrow(() -> new NotFoundException("Order not found"));

        return ResponseEntity.ok().build();
    }
}
