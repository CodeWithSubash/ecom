package com.softwebdevelopers.ecommerce.controller;

import com.softwebdevelopers.ecommerce.business.OCouponBL;
import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Message;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.enums.EUserType;
import com.softwebdevelopers.ecommerce.models.order.Coupon;
import com.softwebdevelopers.ecommerce.security.WebSecurityConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(WebSecurityConfig.BASE_URL + CouponResource.AUTHENTICATED_PATH)
public class CouponResource {

    public static final String AUTHENTICATED_PATH = "/auth/coupon";

    @Autowired
    OCouponBL service;

    @GetMapping()
    public ResponseEntity<PaginationRecordsDto<OCouponDto>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "id") String orderBy,
            @RequestParam(defaultValue = "desc") String orderType,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) throws RecordNotFoundException {

        PaginationRecordsDto<OCouponDto> list = service.getAll(new PagingSortingAndSearchDto().toBuilder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .orderBy(orderBy)
                .orderType(orderType)
                .keyword(keyword)
                .build());
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }


    @GetMapping("/{uuid}")
    public ResponseEntity<OCouponDto> getById(@PathVariable String uuid) throws RecordNotFoundException {
        OCouponDto entity = service.getById(uuid);
        return new ResponseEntity<>(entity, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/getByCouponCode/{couponCode}")
    public ResponseEntity<?> getByCouponCode(@PathVariable String couponCode) throws RecordNotFoundException {
        OCouponDto coupon = service.getByCouponCode(couponCode);
        if(coupon == null) {
            log.warn("The coupon code [{}] fetched failed.", couponCode);
            return new ResponseEntity<>(Message.error(ECOMMessage.COUPON_NOT_VALID), new HttpHeaders(), HttpStatus.PRECONDITION_FAILED);
        }
        return new ResponseEntity<>(coupon, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> create(@Validated @RequestBody OCouponDto coupon) {

        OCouponDto created = service.create(coupon);
        if (created == null) {
            log.warn("The coupon [0] creation failed.");
            return new ResponseEntity<>(Message.error(ECOMMessage.CREATION_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(created, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> disposeById(@PathVariable String uuid) throws RecordNotFoundException {
        OCouponDto disposed = service.dispose(uuid);
        if(disposed == null) {
            log.warn("The user id: [{}] update failed", uuid);
            return new ResponseEntity<>(Message.success(ECOMMessage.UPDATE_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(disposed, new HttpHeaders(), HttpStatus.OK);
    }


}
