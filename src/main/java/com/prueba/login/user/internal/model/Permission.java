package com.prueba.login.user.internal.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un permiso dentro del sistema de seguridad.
 */
@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    @Enumerated(EnumType.STRING)
    private PermissionName name;
}
