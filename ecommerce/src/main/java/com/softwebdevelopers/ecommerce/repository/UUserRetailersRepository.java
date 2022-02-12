package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.user.UserRetailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UUserRetailersRepository extends JpaRepository<UserRetailer, Long> {

    @Query(value = "SELECT * FROM user_retailers WHERE deleted_date IS NULL AND status = 1", nativeQuery = true)
    List<UserRetailer> getAll();
}
