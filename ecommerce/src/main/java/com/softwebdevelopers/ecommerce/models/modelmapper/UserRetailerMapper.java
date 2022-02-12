package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.PaginationInfoDto;
import com.softwebdevelopers.ecommerce.dto.PaginationRecordsDto;
import com.softwebdevelopers.ecommerce.dto.UUserRetailerDto;
import com.softwebdevelopers.ecommerce.models.user.UserRetailer;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRetailerMapper {

    public UUserRetailerDto toDto(UserRetailer entity) {
        return new UUserRetailerDto().toBuilder()
                .id(entity.getId())
                .userId(Preconditions.checkNotNull(entity.getUser()) ? entity.getUser().getId() : null)
                .logo(entity.getLogo())
                .businessName(entity.getBusinessName())
                .tradingStatus(entity.getTradingStatus())
                .email(entity.getEmail())
                .businessAddress(entity.getBusinessAddress())
                .mobile(entity.getMobile())
                .landline(entity.getLandline())
                .fax(entity.getFax())
                .dateOfCorporation(entity.getDateOfCorporation())
                .shareholders(entity.getShareHolders())
                .directors(entity.getDirectors())
                .bankName(entity.getBankName())
                .bankAddress(entity.getBankAddress())
                .bankerContactPerson(entity.getBankerContactPerson())
                .bankerContactNumber(entity.getBankerContactNumber())
                .documentIdNumber(entity.getDocumentIdNumber())
                .documentIdCertificate(entity.getDocumentIdCertificate())
                .businessRegistrationNumber(entity.getBusinessRegistrationNumber())
                .businessRegistrationCertificate(entity.getBusinessRegistrationCertificate())
                .vatRegistrationNumber(entity.getBusinessRegistrationNumber())
                .vatRegistrationCertificate(entity.getVatRegistrationCertificate())
                .notes(entity.getNotes())
                .status(entity.isStatus())
                .deletedAt(entity.getDeletedDate())
                .build();
    }

    public UserRetailer toEntity(UUserRetailerDto dto) {
        return new UserRetailer().toBuilder()
                .id(dto.getId())
                .logo(dto.getLogo())
                .businessName(dto.getBusinessName())
                .tradingStatus(dto.getTradingStatus())
                .email(dto.getEmail())
                .businessAddress(dto.getBusinessAddress())
                .mobile(dto.getMobile())
                .landline(dto.getLandline())
                .fax(dto.getFax())
                .dateOfCorporation(dto.getDateOfCorporation())
                .shareHolders(dto.getShareholders())
                .directors(dto.getDirectors())
                .bankName(dto.getBankName())
                .bankAddress(dto.getBankAddress())
                .bankerContactPerson(dto.getBankerContactPerson())
                .bankerContactNumber(dto.getBankerContactNumber())
                .documentIdNumber(dto.getDocumentIdNumber())
                .documentIdCertificate(dto.getDocumentIdCertificate())
                .businessRegistrationNumber(dto.getBusinessRegistrationNumber())
                .businessRegistrationCertificate(dto.getBusinessRegistrationCertificate())
                .vatRegistrationNumber(dto.getBusinessRegistrationNumber())
                .vatRegistrationCertificate(dto.getVatRegistrationCertificate())
                .notes(dto.getNotes())
                .status(dto.isStatus())
                .deletedDate(dto.getDeletedAt())
                .build();
    }

    public List<UUserRetailerDto> toDto(List<UserRetailer> infoList) {
        List<UUserRetailerDto> dtoList = new ArrayList<>();
        for (UserRetailer entity : infoList) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }

    public PaginationRecordsDto<UUserRetailerDto> toDto(Page<UserRetailer> entityList) {
        List<UUserRetailerDto> dtoList = new ArrayList<>();
        for (UserRetailer entity : entityList.getContent()) {
            dtoList.add(toDto(entity));
        }

        return new PaginationRecordsDto<UUserRetailerDto>().toBuilder()
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
