package com.drop.shiping.api.drop_shiping_api.auth.controllers;

import java.util.Optional;

import com.drop.shiping.api.drop_shiping_api.auth.dtos.RegisterUserDTO;
import com.drop.shiping.api.drop_shiping_api.auth.mappers.AuthMapper;
import com.drop.shiping.api.drop_shiping_api.auth.services.AuthService;
import com.drop.shiping.api.drop_shiping_api.common.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.drop.shiping.api.drop_shiping_api.users.entities.User;
import com.drop.shiping.api.drop_shiping_api.users.dtos.UserResponseDTO;
import com.drop.shiping.api.drop_shiping_api.users.repositories.UserRepository;
import com.drop.shiping.api.drop_shiping_api.users.services.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;

import static com.drop.shiping.api.drop_shiping_api.security.JwtConfig.*;

@RestController
@RequestMapping("/app/users")
@CrossOrigin(originPatterns = "*")
public class AuthController {
    private final AuthService service;
    private final UserService userService;

    public AuthController(AuthService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegisterUserDTO> create(@Valid @RequestBody RegisterUserDTO user) {
        RegisterUserDTO newUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserDTO> register(@Valid @RequestBody RegisterUserDTO user) {
        RegisterUserDTO newUser = AuthMapper.MAPPER.requestDTOtoNotAdmin(user, false);
        return create(newUser);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserResponseDTO> getUser(@RequestHeader("Token") String token) {
        Optional<User> optionalUser = service.getUser(token);

        return optionalUser
            .map(AuthMapper.MAPPER::userToUserResponse)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new NotFoundException("User Not found"));
    }

    @GetMapping("/token-validation")
    public ResponseEntity<Void> tokenValidation(@RequestHeader("Token") String token) {
        try {
            Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
            return ResponseEntity.ok().build();
        } catch(JwtException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
