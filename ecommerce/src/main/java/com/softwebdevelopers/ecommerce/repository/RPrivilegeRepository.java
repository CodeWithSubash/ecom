package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.role.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RPrivilegeRepository extends JpaRepository<Privilege, Long> {
    Privilege findByName(String name);
}
