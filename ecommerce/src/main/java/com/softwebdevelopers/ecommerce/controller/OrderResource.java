package com.softwebdevelopers.ecommerce.controller;

import com.softwebdevelopers.ecommerce.business.OOrderBL;
import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Message;
import com.softwebdevelopers.ecommerce.dto.OOrderDto;
import com.softwebdevelopers.ecommerce.dto.PaginationRecordsDto;
import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
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
@RequestMapping(WebSecurityConfig.BASE_URL + OrderResource.AUTHENTICATED_PATH)
public class OrderResource {

    public static final String AUTHENTICATED_PATH = "/auth/order";

    @Autowired
    OOrderBL service;

    @GetMapping()
    public ResponseEntity<PaginationRecordsDto<OOrderDto>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String deliveryStatus,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "id") String orderBy,
            @RequestParam(defaultValue = "desc") String orderType,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) throws RecordNotFoundException {

        PaginationRecordsDto<OOrderDto> list = service.getAll(deliveryStatus, startDate, endDate, new PagingSortingAndSearchDto().toBuilder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .orderBy(orderBy)
                .orderType(orderType)
                .keyword(keyword)
                .build());
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OOrderDto> getById(@PathVariable Long id) throws RecordNotFoundException {
        OOrderDto entity = service.getById(id);
        return new ResponseEntity<>(entity, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> create(@Validated @RequestBody OOrderDto order) {

        OOrderDto created = service.create(order, 0L);
        if (created == null) {
            log.warn("The order [0] creation failed.");
            return new ResponseEntity<>(Message.success(ECOMMessage.CREATION_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(created, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/retailer/{retailerUserId}")
    public ResponseEntity<?> createBySalesPerson(
            @Validated @RequestBody OOrderDto order,
            @PathVariable Long retailerUserId) {

        OOrderDto created = service.create(order, retailerUserId);
        if (created == null) {
            log.warn("The order [0] creation failed.");
            return new ResponseEntity<>(Message.success(ECOMMessage.CREATION_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(created, new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping("/{deliveryStatus}/{id}")
    public ResponseEntity<?> updateDeliveryStatus(@PathVariable String deliveryStatus, @PathVariable Long id) {
        OOrderDto updated = service.updateDeliveryStatus(id, deliveryStatus);
        if (updated == null) {
            log.warn("The order [0] creation failed.");
            return new ResponseEntity<>(Message.success(ECOMMessage.UPDATE_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(updated, new HttpHeaders(), HttpStatus.OK);
    }
}
