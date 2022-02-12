package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.common.CheckInCheckOutLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface CheckInCheckOutLogRepository extends JpaRepository<CheckInCheckOutLog, UUID> {

    Page<CheckInCheckOutLog> findByUserId(Long userId, Pageable pageable);

    Page<CheckInCheckOutLog> findAllByUserIdAndCheckInTimeGreaterThanEqualAndCheckInTimeLessThanEqual(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<CheckInCheckOutLog> findAllByCheckInTimeGreaterThanEqualAndCheckInTimeLessThanEqual(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
