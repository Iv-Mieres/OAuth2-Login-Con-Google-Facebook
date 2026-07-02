package com.prueba.login.user.internal.repository;

import com.prueba.login.user.internal.model.Permission;
import com.prueba.login.user.internal.model.PermissionName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para operaciones de persistencia de permisos.
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    /**
     * Busca un permiso por su nombre.
     */
    Optional<Permission> findByName(PermissionName name);

    /**
     * Verifica si existe un permiso con el nombre dado.
     */
    boolean existsByName(PermissionName name);
}
