package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.CheckInCheckOutLogDto;
import com.softwebdevelopers.ecommerce.dto.PBrandDto;
import com.softwebdevelopers.ecommerce.dto.PaginationInfoDto;
import com.softwebdevelopers.ecommerce.dto.PaginationRecordsDto;
import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.common.CheckInCheckOutLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CheckInCheckOutLogMapper {

    @Autowired
    UserMapper userMapper;

    public CheckInCheckOutLogDto toDto(CheckInCheckOutLog entity) {
        if(Preconditions.checkNull(entity))
            return null;

        return new CheckInCheckOutLogDto().toBuilder()
                .uuid(entity.getUuid())
                .retailerUserId(entity.getRetailerUser().getId())
                .checkInLat(entity.getCheckInLat())
                .checkInLong(entity.getCheckInLong())
                .checkOutLat(entity.getCheckOutLat())
                .checkOutLong(entity.getCheckOutLong())
                .checkInLocation(entity.getCheckInLocation())
                .checkOutLocation(entity.getCheckOutLocation())
                .checkInTime(entity.getCheckInTime())
                .checkOutTime(entity.getCheckOutTime())
                .user(userMapper.toResponseDto(entity.getUser()))
                .retailerUser(userMapper.toResponseDto(entity.getRetailerUser()))
                .build();
    }

    public CheckInCheckOutLog toEntity(CheckInCheckOutLogDto dto) {
        if(Preconditions.checkNull(dto))
            return null;

        return new CheckInCheckOutLog().toBuilder()
                .checkInLat(dto.getCheckInLat())
                .checkInLong(dto.getCheckInLong())
                .checkOutLat(dto.getCheckInLat())
                .checkOutLong(dto.getCheckInLong())
                .checkInLocation(dto.getCheckInLocation())
                .checkOutLocation(dto.getCheckInLocation())
                .checkInTime(dto.getCheckInTime())
                .checkOutTime(dto.getCheckInTime())
                .build();
    }

    public PaginationRecordsDto<CheckInCheckOutLogDto> toDto(Page<CheckInCheckOutLog> entityList) {
        if(Preconditions.checkNull(entityList))
            return null;

        List<CheckInCheckOutLogDto> dtoList = new ArrayList<>();
        for (CheckInCheckOutLog entity : entityList.getContent()) {
            dtoList.add(toDto(entity));
        }

        return new PaginationRecordsDto<CheckInCheckOutLogDto>().toBuilder()
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
