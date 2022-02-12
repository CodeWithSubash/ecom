package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.dto.UUserRetailerRecordDto;
import com.softwebdevelopers.ecommerce.models.user.UserRetailerRecord;
import org.springframework.stereotype.Component;

@Component
public class UserRetailerRecordMapper {

    public UUserRetailerRecordDto toDto(UserRetailerRecord entity) {
        return new UUserRetailerRecordDto().toBuilder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .businessName(entity.getBusinessName())
                .orderCount(entity.getOrderCount())
                .totalAmount(entity.getTotalAmount())
                .wishlistCount(entity.getWishlistCount())
                .build();
    }
}
