package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.dto.UUserEmployeeRecordDto;
import com.softwebdevelopers.ecommerce.models.user.UserEmployeeRecord;
import org.springframework.stereotype.Component;

@Component
public class UserEmployeeRecordMapper {

    public UUserEmployeeRecordDto toDto(UserEmployeeRecord entity) {
        return new UUserEmployeeRecordDto().toBuilder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .designation(entity.getDesignation())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .mobile(entity.getMobile())
                .city(entity.getCity())
                .state(entity.getState())
                .zipCode(entity.getZipCode())
                .commission(entity.getCommission())
                .totalOrder(entity.getTotalOrder())
                .totalRevenue(entity.getTotalRevenue() - entity.getTotalDiscountAmount())
                .totalAveragePrice(entity.getTotalOrder() > 0 ? (entity.getTotalRevenue() - entity.getTotalDiscountAmount())/entity.getTotalOrder() : 0)
                .currentOrder(entity.getCurrentOrder())
                .currentRevenue(entity.getCurrentTotalAmount() - entity.getCurrentDiscountAmount())
                .previousOrder(entity.getPreviousOrder())
                .previousRevenue(entity.getPreviousTotalAmount() - entity.getPreviousDiscountAmount())
                .currentEarning(entity.getCommission() > 0 ? ((entity.getCurrentTotalAmount() - entity.getCurrentDiscountAmount()) * entity.getCommission()) / 100 : 0)
                .previousEarning(entity.getCommission() > 0 ? ((entity.getPreviousTotalAmount() - entity.getPreviousDiscountAmount()) * entity.getCommission()) / 100 : 0)
                .build();
    }
}
