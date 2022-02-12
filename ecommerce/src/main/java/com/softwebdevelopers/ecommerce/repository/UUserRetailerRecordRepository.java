package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.user.UserEmployeeRecord;
import com.softwebdevelopers.ecommerce.models.user.UserRetailerRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface UUserRetailerRecordRepository extends JpaRepository<UserRetailerRecord, Long> {

    Optional<UserRetailerRecord> findByUserId(Long userId);
}
