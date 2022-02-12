package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RRoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
