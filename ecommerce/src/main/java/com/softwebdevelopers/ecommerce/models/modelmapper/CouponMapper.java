package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.models.order.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CouponMapper {

    @Autowired
    CategoryMapper mapper;

    public OCouponDto toDto(Coupon entity) {

        if (Preconditions.checkNull(entity))
            return null;

        return new OCouponDto().toBuilder()
                .uuid(entity.getUuid())
                .category(Preconditions.checkNotNull(entity.getCategory()) ? mapper.toDto(entity.getCategory()) : null)
                .couponCode(entity.getCouponCode())
                .couponType(entity.getCouponType())
                .discountValue(entity.getDiscountValue())
                .minimumOrderValue(entity.getMinimumOrderValue())
                .maximumDiscountAmount(entity.getMaximumDiscountAmount())
                .isRedeemAllowed(entity.isRedeemAllowed())
                .validFrom(entity.getValidFrom())
                .validUntil(entity.getValidUntil())
                .createdAt(entity.getCreatedDate())
                .build();

    }

    public Coupon toEntity(OCouponDto dto) {
        if(Preconditions.checkNull(dto))
            return null;

        return new Coupon().toBuilder()
                .uuid(dto.getUuid())
                .category(Preconditions.checkNotNull(dto.getCategory()) ? mapper.toEntity(dto.getCategory()) : null)
                .couponCode(dto.getCouponCode().toUpperCase())
                .couponType(dto.getCouponType())
                .discountValue(dto.getDiscountValue())
                .minimumOrderValue(dto.getMinimumOrderValue())
                .maximumDiscountAmount(dto.getMaximumDiscountAmount())
                .isRedeemAllowed(dto.isRedeemAllowed())
                .validFrom(dto.getValidFrom())
                .validUntil(dto.getValidUntil())
                .build();
    }

    public List<OCouponDto> toDto(List<Coupon> entityList) {
        if(Preconditions.checkNull(entityList))
            return null;

        List<OCouponDto> dtoList = new ArrayList<>();
        for (Coupon category : entityList) {
            dtoList.add(toDto(category));
        }
        return dtoList;
    }

    public PaginationRecordsDto<OCouponDto> toDto(Page<Coupon> entityList) {
        if(Preconditions.checkNull(entityList))
            return null;

        List<OCouponDto> dtoList = new ArrayList<>();
        for (Coupon entity : entityList.getContent()) {
            dtoList.add(toDto(entity));
        }

        return new PaginationRecordsDto<OCouponDto>().toBuilder()
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
