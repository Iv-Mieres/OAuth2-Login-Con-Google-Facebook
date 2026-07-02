package com.prueba.login.user.application;

import com.prueba.login.user.api.dto.request.CreateUserRequest;
import com.prueba.login.user.api.dto.request.UpdateUserRequest;
import com.prueba.login.user.api.dto.response.UserResponse;
import com.prueba.login.user.internal.model.*;
import com.prueba.login.user.internal.repository.RoleRepository;
import com.prueba.login.user.internal.repository.UserRepository;
import com.prueba.login.user.internal.exceptions.ResourceNotFoundException;
import com.prueba.login.user.internal.exceptions.BadRequestException;
import org.springframework.modulith.ApplicationModule;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio de aplicación para la gestión de usuarios.
 * Implementa la lógica de negocio del módulo User.
 */
@Service
@ApplicationModule
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, 
                      RoleRepository roleRepository, 
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     */
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BadRequestException("El nombre de usuario ya está en uso");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("El email ya está registrado");
        }

        CustomerUser user = CustomerUser.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .enabled(true)
                .authProvider(AuthProvider.LOCAL)
                .build();

        // Asignar rol por defecto
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));
        user.setRoles(Set.of(userRole));

        CustomerUser savedUser = userRepository.save(user);
        return toUserResponse(savedUser);
    }

    /**
     * Obtiene un usuario por su ID.
     */
    public UserResponse getUserById(Long id) {
        CustomerUser user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return toUserResponse(user);
    }

    /**
     * Obtiene un usuario por su nombre de usuario.
     */
    public UserResponse getUserByUsername(String username) {
        CustomerUser user = userRepository.findByUsernameWithRolesAndPermissions(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
        return toUserResponse(user);
    }

    /**
     * Obtiene todos los usuarios del sistema.
     */
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza un usuario existente parcialmente.
     */
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        CustomerUser user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        if (request.username() != null && !request.username().isBlank()) {
            if (!user.getUsername().equals(request.username()) && userRepository.existsByUsername(request.username())) {
                throw new BadRequestException("El nombre de usuario ya está en uso");
            }
            user.setUsername(request.username());
        }

        if (request.email() != null && !request.email().isBlank()) {
            if (!user.getEmail().equals(request.email()) && userRepository.existsByEmail(request.email())) {
                throw new BadRequestException("El email ya está registrado");
            }
            user.setEmail(request.email());
        }

        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        if (request.enabled() != null) {
            user.setEnabled(request.enabled());
        }

        CustomerUser updatedUser = userRepository.save(user);
        return toUserResponse(updatedUser);
    }

    /**
     * Elimina un usuario por su ID.
     */
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Convierte una entidad CustomerUser a un DTO de respuesta.
     */
    private UserResponse toUserResponse(CustomerUser user) {
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        Set<String> permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> permission.getName().name())
                .collect(Collectors.toSet());

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getEnabled(),
                roles,
                permissions,
                user.getAuthProvider().name(),
                user.getProviderId()
        );
    }
}
