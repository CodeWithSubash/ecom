package com.softwebdevelopers.ecommerce.controller;

import com.softwebdevelopers.ecommerce.business.UUserEmployeeBL;
import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Message;
import com.softwebdevelopers.ecommerce.dto.PaginationRecordsDto;
import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.dto.UUserEmployeeDto;
import com.softwebdevelopers.ecommerce.dto.UUserEmployeeRecordDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.user.UserEmployeeRecord;
import com.softwebdevelopers.ecommerce.security.WebSecurityConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(WebSecurityConfig.BASE_URL + UserEmployeeResource.AUTHENTICATED_PATH)
public class UserEmployeeResource {

    public static final String AUTHENTICATED_PATH = "/auth/userinformation";

    @Autowired
    private UUserEmployeeBL service;

    @GetMapping()
    public ResponseEntity<PaginationRecordsDto<UUserEmployeeDto>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "id") String orderBy,
            @RequestParam(defaultValue = "desc") String orderType,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) throws RecordNotFoundException {

        PaginationRecordsDto<UUserEmployeeDto> list = service.getAll(new PagingSortingAndSearchDto().toBuilder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .orderBy(orderBy)
                .orderType(orderType)
                .keyword(keyword)
                .build());
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UUserEmployeeDto> getById(@PathVariable Long id) throws RecordNotFoundException {
        UUserEmployeeDto entity = service.getById(id);
        return new ResponseEntity<>(entity, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/employeeRecord")
    @Transactional(readOnly = false)
    public ResponseEntity<?> getEmployeeRecord() throws RecordNotFoundException {
        UUserEmployeeRecordDto entity = service.getEmployeeRecord();
        return new ResponseEntity<>(entity, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody UUserEmployeeDto userinfo) {
        UUserEmployeeDto created = service.create(userinfo);
        if(created == null) {
            log.warn("The user info [{}] creation failed", userinfo.getUsername());
            return new ResponseEntity<>(Message.success(ECOMMessage.CREATION_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(created, new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@RequestBody UUserEmployeeDto userinfo, @PathVariable Long id) {
        userinfo.setId(id);
        UUserEmployeeDto updated = service.update(userinfo);
        if(updated == null) {
            log.warn("The user info [{}] update failed", userinfo.getUsername());
            return new ResponseEntity<>(Message.success(ECOMMessage.UPDATE_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(updated, new HttpHeaders(), HttpStatus.OK);
    }
}
