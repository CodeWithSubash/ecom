package com.softwebdevelopers.ecommerce.services;

import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.common.AuditActivityRecord;
import org.springframework.data.domain.Page;

public interface IAuditActivityRecordService {

    AuditActivityRecord create(AuditActivityRecord entity);

    Page<AuditActivityRecord> getAll(int page, int size) throws RecordNotFoundException;

    Page<AuditActivityRecord> getByKeyword(String keyword, int page, int size) throws RecordNotFoundException;
}
