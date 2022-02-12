package com.softwebdevelopers.ecommerce.controller;

import com.softwebdevelopers.ecommerce.business.PBrandBL;
import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Message;
import com.softwebdevelopers.ecommerce.dto.PBrandDto;
import com.softwebdevelopers.ecommerce.dto.PaginationRecordsDto;
import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.security.WebSecurityConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(WebSecurityConfig.BASE_URL + BrandResource.AUTHENTICATED_PATH)
public class BrandResource {

    public static final String AUTHENTICATED_PATH = "/auth/brand";

    @Autowired
    PBrandBL service;

    @GetMapping("/list")
    public ResponseEntity<?> getBrandList() throws RecordNotFoundException {
        List<?> list = service.getAllList();
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<PaginationRecordsDto<PBrandDto>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "id") String orderBy,
            @RequestParam(defaultValue = "desc") String orderType,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) throws RecordNotFoundException {

        PaginationRecordsDto<PBrandDto> list = service.getAll(new PagingSortingAndSearchDto().toBuilder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .orderBy(orderBy)
                .orderType(orderType)
                .keyword(keyword)
                .build());
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<PaginationRecordsDto<PBrandDto>> getAllActive(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "id") String orderBy,
            @RequestParam(defaultValue = "desc") String orderType,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) throws RecordNotFoundException {
        PaginationRecordsDto<PBrandDto> list = service.getAllActive(new PagingSortingAndSearchDto().toBuilder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .orderBy(orderBy)
                .orderType(orderType)
                .keyword(keyword)
                .build());
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/inactive")
    public ResponseEntity<PaginationRecordsDto<PBrandDto>> getAllInactive(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "id") String orderBy,
            @RequestParam(defaultValue = "desc") String orderType,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) throws RecordNotFoundException {
        PaginationRecordsDto<PBrandDto> list = service.getAllInactive(new PagingSortingAndSearchDto().toBuilder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .orderBy(orderBy)
                .orderType(orderType)
                .keyword(keyword)
                .build());
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PBrandDto> getById(@PathVariable Long id) throws RecordNotFoundException {
        PBrandDto entity = service.getById(id);
        return new ResponseEntity<>(entity, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> create(@ModelAttribute PBrandDto brand) throws IOException, FileUploadException {

        PBrandDto created = service.create(brand);
        if (created == null) {
            log.warn("The brand [{}] creation failed", brand.getName());
            return new ResponseEntity<>(Message.success(ECOMMessage.CREATION_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(created, new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@ModelAttribute PBrandDto brand, @PathVariable Long id) throws RecordNotFoundException {
        PBrandDto updated = service.updateById(brand, id);
        if (updated == null) {
            log.warn("The brand id: [{}] update failed", id);
            return new ResponseEntity<>(Message.success(ECOMMessage.UPDATE_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(updated, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) throws RecordNotFoundException {
        try {
            service.deleteById(id);
            return new ResponseEntity<>(Message.success(ECOMMessage.RECORD_DELETED), new HttpHeaders(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(Message.warn(ECOMMessage.RECORD_DOES_NOT_EXIST), new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
