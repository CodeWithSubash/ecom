package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.common.AuditActivityRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditActivityRecordRepository extends JpaRepository<AuditActivityRecord, UUID> {

    @Query(value = "SELECT * FROM audit_activity_record WHERE advisor_id = ?1 ORDER BY audit_timestamp DESC",
            countQuery = "SELECT count(*) FROM audit_activity_record WHERE advisor_id = ?1",
            nativeQuery = true)
    Page<AuditActivityRecord> findAllWithPagination(Long advisorId, Pageable pageable);

    @Query(value = "SELECT * FROM audit_activity_record WHERE advisor_id = ?1 AND as_string LIKE %?2% ORDER BY audit_timestamp DESC",
            countQuery = "SELECT count(*) FROM audit_activity_record WHERE advisor_id = ?1 AND as_string LIKE %?2%",
            nativeQuery = true)
    Page<AuditActivityRecord> findByKeywordWithPagination(Long advisorId, String keyword, Pageable pageable);


}
