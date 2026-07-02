package com.prueba.login.user.internal.repository;

import com.prueba.login.user.internal.model.CustomerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para operaciones de persistencia de usuarios.
 */
@Repository
public interface UserRepository extends JpaRepository<CustomerUser, Long> {

    /**
     * Busca un usuario por su nombre de usuario.
     */
    Optional<CustomerUser> findByUsername(String username);

    /**
     * Busca un usuario por su email.
     */
    Optional<CustomerUser> findByEmail(String email);

    /**
     * Verifica si existe un usuario con el nombre de usuario dado.
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si existe un usuario con el email dado.
     */
    boolean existsByEmail(String email);

    /**
     * Busca un usuario por nombre de usuario incluyendo sus roles y permisos.
     */
    @Query("SELECT u FROM CustomerUser u LEFT JOIN FETCH u.roles r LEFT JOIN FETCH r.permissions WHERE u.username = :username")
    Optional<CustomerUser> findByUsernameWithRolesAndPermissions(@Param("username") String username);

    /**
     * Busca un usuario por email incluyendo sus roles y permisos.
     */
    @Query("SELECT u FROM CustomerUser u LEFT JOIN FETCH u.roles r LEFT JOIN FETCH r.permissions WHERE u.email = :email")
    Optional<CustomerUser> findByEmailWithRolesAndPermissions(@Param("email") String email);
}
