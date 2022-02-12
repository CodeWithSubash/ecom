package com.softwebdevelopers.ecommerce.business;

import com.softwebdevelopers.ecommerce.dto.AuditActivityRecordDto;
import com.softwebdevelopers.ecommerce.dto.PaginationRecordsDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.common.AuditActivityRecord;
import com.softwebdevelopers.ecommerce.models.modelmapper.AuditActivityRecordMapper;
import com.softwebdevelopers.ecommerce.services.IAuditActivityRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class AuditActivityRecordBL {

    @Autowired
    IAuditActivityRecordService service;

    @Autowired
    AuditActivityRecordMapper mapper;

    public PaginationRecordsDto<AuditActivityRecordDto> getAll(int page, int size) throws RecordNotFoundException {
        Page<AuditActivityRecord> list = service.getAll(page, size);
        return mapper.toDto(list);
    }

    public PaginationRecordsDto<AuditActivityRecordDto> getByKeyword(String keyword, int page, int size) throws RecordNotFoundException {
        Page<AuditActivityRecord> list = service.getByKeyword(keyword, page, size);
        return mapper.toDto(list);
    }

    public AuditActivityRecordDto create(AuditActivityRecordDto dto) {
        AuditActivityRecord created = service.create(mapper.toEntity(dto));
        return mapper.toDto(created);
    }
}
