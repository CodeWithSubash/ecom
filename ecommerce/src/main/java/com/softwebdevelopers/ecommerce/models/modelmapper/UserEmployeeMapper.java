package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.PaginationInfoDto;
import com.softwebdevelopers.ecommerce.dto.PaginationRecordsDto;
import com.softwebdevelopers.ecommerce.dto.UUserEmployeeDto;
import com.softwebdevelopers.ecommerce.dto.UUserEmployeeRecordDto;
import com.softwebdevelopers.ecommerce.models.user.UserEmployee;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class UserEmployeeMapper {

    public UUserEmployeeDto toDto(UserEmployee entity) {
        return new UUserEmployeeDto().toBuilder()
                .id(entity.getId())
                .userId(Preconditions.checkNotNull(entity.getUser()) ? entity.getUser().getId() : null)
                .username(entity.getUsername())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .designation(entity.getDesignation())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .mobile(entity.getMobile())
                .alternativePhone(entity.getAlternativePhone())
                .alternativeMobile(entity.getAlternativeMobile())
                .street(entity.getStreet())
                .street2(entity.getStreet2())
                .city(entity.getCity())
                .state(entity.getState())
                .zipCode(entity.getZipCode())
                .commission(entity.getCommission())
                .build();
    }

    public UserEmployee toEntity(UUserEmployeeDto dto) {
        return new UserEmployee().toBuilder()
                .id(dto.getId())
                .username(dto.getUsername())
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .designation(dto.getDesignation())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .mobile(dto.getMobile())
                .alternativePhone(dto.getAlternativePhone())
                .alternativeMobile(dto.getAlternativeMobile())
                .street(dto.getStreet())
                .street2(dto.getStreet2())
                .city(dto.getCity())
                .state(dto.getState())
                .zipCode(dto.getZipCode())
                .commission(dto.getCommission())
                .build();
    }

    public List<UUserEmployeeDto> toDto(List<UserEmployee> infoList) {
        List<UUserEmployeeDto> dtoList = new ArrayList<>();
        for (UserEmployee entity : infoList) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }

    public PaginationRecordsDto<UUserEmployeeDto> toDto(Page<UserEmployee> entityList) {
        List<UUserEmployeeDto> dtoList = new ArrayList<>();
        for (UserEmployee entity : entityList.getContent()) {
            dtoList.add(toDto(entity));
        }

        return new PaginationRecordsDto<UUserEmployeeDto>().toBuilder()
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

//    public UUserEmployeeRecordDto toDto(Map<String, Object> entity) {
//        return new UUserEmployeeDto().toBuilder()
//                .id(entity.getId())
//                .userId(Preconditions.checkNotNull(entity.getUser()) ? entity.getUser().getId() : null)
//                .username(entity.getUsername())
//                .firstname(entity.getFirstname())
//                .lastname(entity.getLastname())
//                .designation(entity.getDesignation())
//                .email(entity.getEmail())
//                .phone(entity.getPhone())
//                .mobile(entity.getMobile())
//                .alternativePhone(entity.getAlternativePhone())
//                .alternativeMobile(entity.getAlternativeMobile())
//                .street(entity.getStreet())
//                .street2(entity.getStreet2())
//                .city(entity.getCity())
//                .state(entity.getState())
//                .zipCode(entity.getZipCode())
//                .commission(entity.getCommission())
//                .build();
//    }

}
