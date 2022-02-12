package com.softwebdevelopers.ecommerce.business;

import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.CheckInCheckOutLogDto;
import com.softwebdevelopers.ecommerce.dto.OOrderDto;
import com.softwebdevelopers.ecommerce.dto.PaginationRecordsDto;
import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.common.CheckInCheckOutLog;
import com.softwebdevelopers.ecommerce.models.modelmapper.CheckInCheckOutLogMapper;
import com.softwebdevelopers.ecommerce.services.ICheckInCheckOutLogService;
import com.softwebdevelopers.ecommerce.utils.ECOMUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Component
public class CheckInCheckOutLogBL {

    @Autowired
    ICheckInCheckOutLogService service;

    @Autowired
    CheckInCheckOutLogMapper mapper;

    public PaginationRecordsDto<CheckInCheckOutLogDto> getAll(String startDate, String endDate, PagingSortingAndSearchDto page) throws RecordNotFoundException {

        if (Preconditions.checkNotBlank(startDate) &&
                Preconditions.checkNotBlank(endDate)) {
            PaginationRecordsDto<CheckInCheckOutLogDto> dto = mapper.toDto(service.getByDateRange(
                    ECOMUtilities.convertToLocalDateTime(startDate, null).toLocalDate().atTime(LocalTime.MIN),
                    ECOMUtilities.convertToLocalDateTime(endDate, null).toLocalDate().atTime(LocalTime.MAX),
                    page));
            return dto;
        } else {
            PaginationRecordsDto<CheckInCheckOutLogDto> dto = mapper.toDto(service.getAll(page));
            return dto;
        }
    }

    public PaginationRecordsDto<CheckInCheckOutLogDto> getByUserId(Long userId, PagingSortingAndSearchDto page) {
        PaginationRecordsDto<CheckInCheckOutLogDto> dto = mapper.toDto(service.getByUserId(userId, page));
        return dto;
    }

    public CheckInCheckOutLogDto getById(UUID uuid) throws RecordNotFoundException {
        CheckInCheckOutLogDto dto = mapper.toDto(service.getById(uuid));

        return dto;
    }

    public CheckInCheckOutLogDto create(CheckInCheckOutLogDto dto) {
        dto.setCheckInTime(LocalDateTime.now());
        CheckInCheckOutLog created = service.create(mapper.toEntity(dto), dto.getRetailerUserId());
        return mapper.toDto(created);
    }
}
