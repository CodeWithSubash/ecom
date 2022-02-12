package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.dto.AuditActivityRecordDto;
import com.softwebdevelopers.ecommerce.dto.PaginationInfoDto;
import com.softwebdevelopers.ecommerce.dto.PaginationRecordsDto;
import com.softwebdevelopers.ecommerce.models.common.AuditActivityRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuditActivityRecordMapper {

    public AuditActivityRecord toEntity(AuditActivityRecordDto dto) {
        return new AuditActivityRecord().toBuilder().username(dto.getUsername()).clazz(dto.getClazz())
                .method(dto.getMethod()).asString(dto.getAsString()).build();
    }

    public AuditActivityRecordDto toDto(AuditActivityRecord entity) {
        return new AuditActivityRecordDto().toBuilder().uuid(entity.getUuid()).username(entity.getUsername())
                .clazz(entity.getClazz()).method(entity.getMethod()).asString(entity.getAsString())
                .auditTimestamp(entity.getAuditTimestamp()).build();
    }

    public List<AuditActivityRecord> toEntity(List<AuditActivityRecordDto> dtos) {
        List<AuditActivityRecord> entityList = new ArrayList<>();
        for (AuditActivityRecordDto dto : dtos) {
            entityList.add(toEntity(dto));
        }
        return entityList;
    }

    public List<AuditActivityRecordDto> toDto(List<AuditActivityRecord> entityList) {
        List<AuditActivityRecordDto> dtoList = new ArrayList<>();
        for (AuditActivityRecord entity : entityList) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }

    public PaginationRecordsDto<AuditActivityRecordDto> toDto(Page<AuditActivityRecord> entityList) {
        List<AuditActivityRecordDto> dtoList = new ArrayList<>();
        for (AuditActivityRecord entity : entityList.getContent()) {
            dtoList.add(toDto(entity));
        }

        return new PaginationRecordsDto<AuditActivityRecordDto>().toBuilder()
                .paginationInfo(new PaginationInfoDto().toBuilder()
                        .currentPage(entityList.getNumber())
                        .totalItems(entityList.getTotalElements())
                        .totalPages(entityList.getTotalPages())
                        .size(entityList.getSize())
                        .numberOfItems(entityList.getNumberOfElements())
                        .isFirst(entityList.isFirst())
                        .isLast(entityList.isLast())
                        .hasNext(entityList.hasNext())
                        .hasPrevious(entityList.hasPrevious())
                        .build())
                .data(dtoList)
                .build();

    }
}
