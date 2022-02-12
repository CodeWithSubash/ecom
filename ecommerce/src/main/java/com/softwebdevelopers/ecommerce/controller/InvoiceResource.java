package com.softwebdevelopers.ecommerce.controller;

import com.softwebdevelopers.ecommerce.business.OInvoiceBL;
import com.softwebdevelopers.ecommerce.business.OOrderBL;
import com.softwebdevelopers.ecommerce.dto.OInvoiceDto;
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
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(WebSecurityConfig.BASE_URL + InvoiceResource.AUTHENTICATED_PATH)
public class InvoiceResource {

    public static final String AUTHENTICATED_PATH = "/auth/invoice";

    @Autowired
    OInvoiceBL service;

    @GetMapping()
    public ResponseEntity<PaginationRecordsDto<OInvoiceDto>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String paymentStatus,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "id") String orderBy,
            @RequestParam(defaultValue = "desc") String orderType,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) throws RecordNotFoundException {

        PaginationRecordsDto<OInvoiceDto> list = service.getAll(paymentStatus, startDate, endDate, new PagingSortingAndSearchDto().toBuilder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .orderBy(orderBy)
                .orderType(orderType)
                .keyword(keyword)
                .build());
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OInvoiceDto> getById(@PathVariable Long id) throws RecordNotFoundException {
        OInvoiceDto entity = service.getById(id);
        return new ResponseEntity<>(entity, new HttpHeaders(), HttpStatus.OK);
    }
}
