package com.softwebdevelopers.ecommerce.controller;

import com.softwebdevelopers.ecommerce.business.UUserRetailerBL;
import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Message;
import com.softwebdevelopers.ecommerce.dto.UUserRetailerDto;
import com.softwebdevelopers.ecommerce.dto.UUserRetailerRecordDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.security.WebSecurityConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(WebSecurityConfig.BASE_URL + UserRetailerResource.AUTHENTICATED_PATH)
public class UserRetailerResource {

    public static final String AUTHENTICATED_PATH = "/auth/userRetailer";

    @Autowired
    UUserRetailerBL service;

    @GetMapping("/list")
    public ResponseEntity<?> getUserRetailerList() throws RecordNotFoundException {
        List<?> list = service.getAllList();
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping("/status/{retailerId}")
    public ResponseEntity<?> updateStatusById(@PathVariable Long retailerId, @RequestBody UUserRetailerDto retailer) {
        retailer.setId(retailerId);
        UUserRetailerDto updated = service.updateRetailerStatusById(retailer);
        if(updated == null) {
            log.warn("The user kyc [{}] update failed", retailerId);
            return new ResponseEntity<>(Message.success(ECOMMessage.UPDATE_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(updated, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/retailerRecord")
    @Transactional(readOnly = false)
    public ResponseEntity<?> getRetailerRecord() throws RecordNotFoundException {
        UUserRetailerRecordDto entity = service.getRetailerRecord();
        return new ResponseEntity<>(entity, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> updateRetailer(@RequestBody UUserRetailerDto retailer) {
        UUserRetailerDto created = service.update(retailer);
        if(created == null) {
            log.warn("The user retailer [{}] update failed", retailer.getBusinessName());
            return new ResponseEntity<>(Message.success(ECOMMessage.CREATION_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(created, new HttpHeaders(), HttpStatus.OK);
    }
}
