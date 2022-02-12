package com.softwebdevelopers.ecommerce.services;

import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.common.CheckInCheckOutLog;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ICheckInCheckOutLogService {

    CheckInCheckOutLog create(CheckInCheckOutLog entity, Long retailerUserId);

    Page<CheckInCheckOutLog> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException;

    Page<CheckInCheckOutLog> getByDateRange(LocalDateTime startDate, LocalDateTime endDate, PagingSortingAndSearchDto page) throws RecordNotFoundException;

    Page<CheckInCheckOutLog> getByKeyword(String keyword, PagingSortingAndSearchDto page) throws RecordNotFoundException;

    Page<CheckInCheckOutLog> getByUserId(Long userId, PagingSortingAndSearchDto page) throws RecordNotFoundException;

    CheckInCheckOutLog getById(UUID uuid) throws RecordNotFoundException;
}
