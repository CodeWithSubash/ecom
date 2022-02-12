package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.user.UserEmployeeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface UUserEmployeeRecordRepository extends JpaRepository<UserEmployeeRecord, Long> {

    Optional<UserEmployeeRecord> findByUserId(Long userId);
}
