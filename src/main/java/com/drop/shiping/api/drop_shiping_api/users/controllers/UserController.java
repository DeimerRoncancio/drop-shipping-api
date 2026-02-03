package com.drop.shiping.api.drop_shiping_api.users.controllers;

import com.drop.shiping.api.drop_shiping_api.common.exceptions.NotFoundException;
import com.drop.shiping.api.drop_shiping_api.users.dtos.PasswordDTO;
import com.drop.shiping.api.drop_shiping_api.users.dtos.UserDTO;
import com.drop.shiping.api.drop_shiping_api.users.dtos.UserResponseDTO;
import com.drop.shiping.api.drop_shiping_api.users.enums.Field;
import com.drop.shiping.api.drop_shiping_api.users.mappers.UserMapper;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import com.drop.shiping.api.drop_shiping_api.users.entities.User;
import com.drop.shiping.api.drop_shiping_api.users.services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/app/users")
@CrossOrigin(originPatterns = "*")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponseDTO> viewAll(@PageableDefault Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/by-role")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponseDTO> viewByRole(@PageableDefault Pageable pageable, @RequestParam("isAdmin") boolean isAdmin) {
        return service.findByRole(pageable, isAdmin);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> view(@PathVariable("id") String id) {
        Optional<UserResponseDTO> opProduct = service.findOne(id);

        return opProduct.map(product -> ResponseEntity.ok().body(product))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> update(@PathVariable("id") String id, @Valid @RequestBody UserDTO userDTO) {
        Optional<UserDTO> userOptional = service.update(id, userDTO);
        UserDTO user = userOptional.orElseThrow(() -> new NotFoundException("User not found"));

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") String id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO user = UserMapper.MAPPER.userDTOtoOrAdmin(userDTO, false);
        return update(id, user);
    }

    @PutMapping("/update/password/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody PasswordDTO newPassword,
    @PathVariable String id) {
        service.updatePassword(id, newPassword).orElseThrow(() -> new NotFoundException("User nor found"));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        Optional<User> optionalUser = service.delete(id);
        optionalUser.orElseThrow(() -> new NotFoundException("User not found"));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, Long>> userStats(@RequestParam boolean isAdmin) {
        return ResponseEntity.ok().body(service.userStats(isAdmin));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> userSearch(@PageableDefault Pageable pageable, @RequestParam String identifier,
    @RequestParam boolean isAdmin, @RequestParam(required = false) Boolean isEnabled,
    @RequestParam(required = false, defaultValue = "NAME") Field field) {
        return ResponseEntity.ok().body(service.userSearch(pageable, identifier, isAdmin, isEnabled, field));
    }
}
