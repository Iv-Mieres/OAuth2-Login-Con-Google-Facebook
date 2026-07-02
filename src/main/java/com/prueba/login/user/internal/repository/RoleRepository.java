package com.prueba.login.user.internal.repository;

import com.prueba.login.user.internal.model.Role;
import com.prueba.login.user.internal.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para operaciones de persistencia de roles.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Busca un rol por su nombre.
     */
    Optional<Role> findByName(RoleName name);

    /**
     * Verifica si existe un rol con el nombre dado.
     */
    boolean existsByName(RoleName name);
}
