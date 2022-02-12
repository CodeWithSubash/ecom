package com.softwebdevelopers.ecommerce.business;

import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.custom.IRetailerCount;
import com.softwebdevelopers.ecommerce.models.custom.RetailerCount;
import com.softwebdevelopers.ecommerce.models.enums.EUserType;
import com.softwebdevelopers.ecommerce.models.modelmapper.SelectItemsMapper;
import com.softwebdevelopers.ecommerce.models.modelmapper.UserRetailerMapper;
import com.softwebdevelopers.ecommerce.models.modelmapper.UserRetailerRecordMapper;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.models.user.UserEmployee;
import com.softwebdevelopers.ecommerce.models.user.UserRetailer;
import com.softwebdevelopers.ecommerce.repository.OOrderRepository;
import com.softwebdevelopers.ecommerce.services.IUUserRetailerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UUserRetailerBL {

    @Autowired
    IUUserRetailerService service;

    @Autowired
    OOrderRepository orderRepository;

    @Autowired
    SelectItemsMapper itemsMapper;

    @Autowired
    UserRetailerMapper mapper;

    @Autowired
    UserRetailerRecordMapper recordMapper;

    @Autowired
    private AuditActivityRecordBL auditService;

    public List<SelectItemsDto.SelectItemIntDto> getAllList() throws RecordNotFoundException {
        List<SelectItemsDto.SelectItemIntDto> dtoList = new ArrayList<>();
        List<UserRetailer> kycList = service.getAll();
        for (UserRetailer kyc : kycList) {
            dtoList.add(itemsMapper.toDto(kyc.getId(), kyc.getBusinessName()));
        }
        return dtoList;
    }

    public UUserRetailerRecordDto getRetailerRecord() {
        UUserRetailerRecordDto dto = recordMapper.toDto(service.getRetailerRecord());

        List<IRetailerCount> counts = orderRepository.countRetailerTotalOrder(dto.getUserId());
        dto.setMonthlyGraph(counts.stream().map(item -> new RetailerCount().toBuilder()
                .countOrder(item.getCountOrder())
                .oYear(item.getOYear())
                .oMonth(item.getOMonth())
                .monthName(item.getMonthName())
                .totalAmount(item.getTotalAmount())
                .build()).collect(Collectors.toList()));
        return dto;
    }

    public UUserRetailerDto updateRetailerStatusById(UUserRetailerDto retailer) {

        UserRetailer updated = service.updateRetailerById(retailer);
        if (updated == null) {
            log.warn("The user retailer [{}] update failed", retailer.getId());
            return null;
        }

        auditService.create(
                new AuditActivityRecordDto().toBuilder()
                        .clazz(UUserBL.class.toString())
                        .method(ECOMConstants.UPDATED)
                        .asString("Updated Retailer, <b>"
                                .concat(updated.getBusinessName())
                                .concat("</b> with status ")
                                .concat(updated.isStatus() ? "Accepted" : "Rejected"))
                        .build());

        return mapper.toDto(updated);
    }

    public UUserRetailerDto update(UUserRetailerDto user) throws RecordNotFoundException {
        UserRetailer updated = service.update(mapper.toEntity(user));
        if (updated == null) {
            log.warn("The retailer user id: [{}] update failed", user.getId());
            return null;
        }

        auditService.create(
                new AuditActivityRecordDto().toBuilder()
                        .clazz(UUserBL.class.toString())
                        .method(ECOMConstants.UPDATED)
                        .asString("Updated Retailer User, <b>"
                                .concat(updated.getBusinessName())
                                .concat("</b>"))
                        .build());

        return mapper.toDto(updated);
    }
}
