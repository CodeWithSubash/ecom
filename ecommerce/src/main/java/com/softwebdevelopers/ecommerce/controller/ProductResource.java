package com.softwebdevelopers.ecommerce.controller;

import com.softwebdevelopers.ecommerce.business.PProductBL;
import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Message;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.PProductDto;
import com.softwebdevelopers.ecommerce.dto.PaginationRecordsDto;
import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.ECOMException;
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
@RequestMapping(WebSecurityConfig.BASE_URL + ProductResource.AUTHENTICATED_PATH)
public class ProductResource {
    public static final String AUTHENTICATED_PATH = "/auth/product";

    @Autowired
    PProductBL service;

    @GetMapping("/list")
    public ResponseEntity<?> getProductList() throws RecordNotFoundException {
        List<?> list = service.getAllList();
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<PaginationRecordsDto<PProductDto>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "id") String orderBy,
            @RequestParam(defaultValue = "desc") String orderType,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) throws RecordNotFoundException {

        PaginationRecordsDto<PProductDto> list = service.getAll(new PagingSortingAndSearchDto().toBuilder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .orderBy(orderBy)
                .orderType(orderType)
                .keyword(keyword)
                .build());
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<PaginationRecordsDto<PProductDto>> getAllActive(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "id") String orderBy,
            @RequestParam(defaultValue = "desc") String orderType,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) throws RecordNotFoundException {
        PaginationRecordsDto<PProductDto> list = service.getAllActive(new PagingSortingAndSearchDto().toBuilder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .orderBy(orderBy)
                .orderType(orderType)
                .keyword(keyword)
                .build());
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/getByCategory")
    public ResponseEntity<PaginationRecordsDto<PProductDto>> getByCategory(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") Long categoryId,
            @RequestParam(defaultValue = "id") String orderBy,
            @RequestParam(defaultValue = "desc") String orderType,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) throws RecordNotFoundException {
        PaginationRecordsDto<PProductDto> list = service.getAllByCategory(new PagingSortingAndSearchDto().toBuilder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .orderBy(orderBy)
                .orderType(orderType)
                .keyword(keyword)
                .build(), categoryId);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PProductDto> getById(@PathVariable Long id) throws RecordNotFoundException {
        PProductDto entity = service.getById(id);
        return new ResponseEntity<>(entity, new HttpHeaders(), HttpStatus.OK);
    }

//    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Received Invalid Input Parameters")
//    @ExceptionHandler()
    @PostMapping()
    public ResponseEntity<?> create(
            @RequestParam(required = false) String wholesalerId,
            @ModelAttribute PProductDto product) throws IOException, FileUploadException {

        Long id = 0L;
        if(Preconditions.checkNotBlank(wholesalerId)) {
            try{
                id = Long.parseLong(wholesalerId);
            } catch (NumberFormatException e) {
                log.warn("The provided id [{}] is not correct.", wholesalerId);
                throw new ECOMException(
                        "The provided wholesaler Id: [" + wholesalerId + "] is not valid id.");
            }
        }
        PProductDto created = service.create(product, id);
        if (created == null) {
            log.warn("The product [{}] creation failed", product.getName());
            return new ResponseEntity<>(Message.success(ECOMMessage.CREATION_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(created, new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(
            @PathVariable Long id,
            @RequestParam(required = false) String wholesalerId,
            @ModelAttribute PProductDto product) throws RecordNotFoundException {
        Long empId = 0L;
        if(Preconditions.checkNotBlank(wholesalerId)) {
            try{
                empId = Long.parseLong(wholesalerId);
            } catch (NumberFormatException e) {
                log.warn("The provided id [{}] is not correct.", wholesalerId);
                throw new ECOMException(
                        "The provided wholesaler Id: [" + wholesalerId + "] is not valid id.");
            }
        }
        PProductDto updated = service.updateById(product, id, empId);
        if (updated == null) {
            log.warn("The product id: [{}] update failed", id);
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
