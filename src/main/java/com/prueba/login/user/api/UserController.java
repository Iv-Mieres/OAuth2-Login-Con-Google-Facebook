package com.prueba.login.user.api;

import com.prueba.login.user.application.UserService;
import com.prueba.login.user.api.dto.request.CreateUserRequest;
import com.prueba.login.user.api.dto.request.UpdateUserRequest;
import com.prueba.login.user.api.dto.response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para la gestión de usuarios.
 * Expone los endpoints públicos del módulo User.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = userService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Obtiene un usuario por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    /**
     * Obtiene el perfil del usuario autenticado.
     */
    @GetMapping("/my-profile")
    public ResponseEntity<UserResponse> getUserAuthenticated(Authentication authentication) {
        String username = authentication.getName();
        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
    }

    /**
     * Obtiene todos los usuarios del sistema.
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    /**
     * Actualiza un usuario existente parcialmente.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return new ResponseEntity<>(userService.updateUser(id, request), HttpStatus.OK);
    }

    /**
     * Elimina un usuario por su ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
