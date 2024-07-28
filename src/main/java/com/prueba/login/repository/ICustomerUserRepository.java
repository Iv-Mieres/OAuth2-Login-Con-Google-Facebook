package com.prueba.login.repository;

import com.prueba.login.model.CustomerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICustomerUserRepository extends JpaRepository<CustomerUser, Long> {
    Optional<CustomerUser> findByEmail(String email);
    Optional<CustomerUser> findBySocialIdAndProvider(String id, String provider);
    Boolean existsByEmail(String email);
    Boolean existsByEmailAnId(String email, Long id);

}
