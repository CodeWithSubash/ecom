package com.softwebdevelopers.ecommerce.business;

import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.exceptions.ECOMException;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.modelmapper.CouponMapper;
import com.softwebdevelopers.ecommerce.models.order.Coupon;
import com.softwebdevelopers.ecommerce.models.product.Product;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.services.IOCouponService;
import com.softwebdevelopers.ecommerce.utils.ECOMUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OCouponBL {

    @Autowired
    IOCouponService service;

    @Autowired
    PCategoryBL categoryService;

    @Autowired
    CouponMapper mapper;

    @Autowired
    private AuditActivityRecordBL auditService;

    public PaginationRecordsDto<OCouponDto> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        PaginationRecordsDto<OCouponDto> dto = mapper.toDto(service.getAll(page));
        return dto;
    }

    public OCouponDto getById(String uuid) throws RecordNotFoundException {
        if (!Preconditions.checkUUIDFormat(uuid)) {
            throw new ECOMException("The provided UUID is invalid;");
        }

        OCouponDto dto = mapper.toDto(service.getById(UUID.fromString(uuid)));
        return dto;
    }

    public OCouponDto getByCouponCode(String couponCode) {
        OCouponDto dto = mapper.toDto(service.getByCouponCode(couponCode));
        if (dto != null) {
            if (dto.isRedeemAllowed()) {
                if (Preconditions.checkNotNull(dto.getValidFrom())) {
                    if (LocalDateTime.now().compareTo(dto.getValidFrom()) < 0) {
                        return null;
                    }
                }
                if (Preconditions.checkNotNull(dto.getValidUntil())) {
                    if (LocalDateTime.now().compareTo(dto.getValidUntil()) > 0) {
                        return null;
                    }
                }
                if (Preconditions.checkNotNull(dto.getValidFrom()) && Preconditions.checkNotNull(dto.getValidUntil())
                        && LocalDateTime.now().compareTo(dto.getValidFrom()) < 0
                        && LocalDateTime.now().compareTo(dto.getValidUntil()) > 0) {
                    return null;
                }
            } else {
                return null;
            }
        }

        return dto;
    }

    public OCouponDto create(OCouponDto coupon) {

        if (coupon.getCategoryId() != null && coupon.getCategoryId() > 0) {
            coupon.setCategory(categoryService.getById(coupon.getCategoryId()));
        }
        if (Preconditions.checkNotNull(coupon.getValidFrom())) {
            coupon.setValidFrom(coupon.getValidFrom().toLocalDate().atTime(LocalTime.MIN));
        }
        if (Preconditions.checkNotNull(coupon.getValidUntil())) {
            coupon.setValidUntil(coupon.getValidUntil().toLocalDate().atTime(LocalTime.MAX));
        }

        Coupon created = service.create(mapper.toEntity(coupon));
        if (created == null) {
            log.warn("The coupon [{}] creation failed", coupon.getCouponCode());
            return null;
        }

        auditService.create(
                new AuditActivityRecordDto().toBuilder()
                        .clazz(PBrandBL.class.toString())
                        .method(ECOMConstants.CREATED)
                        .asString("Added new <b>Coupon</b>, <b>"
                                .concat(created.getCouponCode())
                                .concat("</b>")
                                .concat(" by ")
                                .concat(Preconditions.checkNotNull(created.getUser()) ? created.getUser().getName() : "Admin"))
                        .build());

        return mapper.toDto(created);
    }

    public OCouponDto dispose(String uuid) throws RecordNotFoundException {

        if (!Preconditions.checkUUIDFormat(uuid)) {
            throw new ECOMException("The provided UUID is invalid;");
        }

        Coupon dispose = service.dispose(uuid);
        if (Preconditions.checkNotNull(dispose)) {
            auditService.create(
                    new AuditActivityRecordDto().toBuilder()
                            .clazz(UUserBL.class.toString())
                            .method(ECOMConstants.DELETED)
                            .asString("Dispose Coupon, <b>"
                                    .concat(dispose.getCouponCode())
                                    .concat("</b>"))
                            .build());
        }

        return mapper.toDto(dispose);
    }
}
