package com.softwebdevelopers.ecommerce.controller;

import com.softwebdevelopers.ecommerce.business.UUserBL;
import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Message;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.enums.EUserType;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.security.WebSecurityConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(WebSecurityConfig.BASE_URL + UserResource.AUTHENTICATED_PATH)
public class UserResource {

    public static final String AUTHENTICATED_PATH = "/auth/user";

    @Autowired
    private UUserBL service;

    /**
     * Get the information of the logged-in user
     *
     * @return
     * @throws RecordNotFoundException
     */
    @GetMapping()
    public ResponseEntity<UUserDto> getUser() throws RecordNotFoundException {
        UUserDto entity = service.getUser();
        return new ResponseEntity<>(entity, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/list/retailer")
    public ResponseEntity<?> getRetailerList() throws RecordNotFoundException {

        List<?> list = service.getAllList(EUserType.RETAILER.name());
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/list/employee")
    public ResponseEntity<?> getEmployeeList() throws RecordNotFoundException {

        List<?> list = service.getAllList(EUserType.EMPLOYEE.name());
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/list/salesRepresentative")
    public ResponseEntity<?> getSalesEmployeeList() throws RecordNotFoundException {

        List<?> list = service.getAllSalesRepresentativeList(EUserType.EMPLOYEE.name());
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/list/wholesaler")
    public ResponseEntity<?> getWholesalerList() throws RecordNotFoundException {

        List<?> list = service.getAllWholesalerList(EUserType.EMPLOYEE.name());
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Get the details of the user from database
     *
     * @return
     */
    @GetMapping("/getAll/{userType}")
    public ResponseEntity<PaginationRecordsDto<UUserDto>> getAllUser(
            @PathVariable String userType,
            @RequestParam(required = false) String keyword,
             @RequestParam(defaultValue = "id") String orderBy,
             @RequestParam(defaultValue = "desc") String orderType,
             @RequestParam(defaultValue = "0") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize) {
        PaginationRecordsDto<UUserDto> list = service.getAllUser(new PagingSortingAndSearchDto().toBuilder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .orderBy(orderBy)
                .orderType(orderType)
                .keyword(keyword)
                .build(), userType);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<Message> changePassword(
            @Validated @RequestBody URegistrationDto registrationDto) {

        if (Preconditions.checkNull(registrationDto.getOldPassword())
                || Preconditions.checkNull(registrationDto.getPassword())
                || registrationDto.getOldPassword().equals(registrationDto.getPassword())) {

            return ResponseEntity.badRequest()
                    .body(ECOMConstants.PASSWORD_NEW_PASSWORD_CANT_SAME);
        }

        if (Preconditions.checkNull(registrationDto.getRePassword())
                || Preconditions.checkNull(registrationDto.getPassword())
                || !registrationDto.getPassword().equals(registrationDto.getRePassword())) {

            return ResponseEntity.badRequest()
                    .body(ECOMConstants.PASSWORD_REPASSWORD_NOT_MATCHED);
        }

        service.changePassword(registrationDto);
        return new ResponseEntity<Message>(ECOMConstants.PASSWORD_CHANGE_SUCCESS, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UUserDto> getById(@PathVariable Long id) throws RecordNotFoundException {
        UUserDto entity = service.getById(id);
        return new ResponseEntity<UUserDto>(entity, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody UUserDto user) {
        UUserDto created = service.create(user);
        if(created == null) {
            log.warn("The user [{}] creation failed", user.getName());
            return new ResponseEntity<>(Message.success(ECOMMessage.CREATION_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(created, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/retailer")
    public ResponseEntity<?> createRetailer(@RequestBody UUserDto user) {
        user.setUserType(EUserType.RETAILER.name());
        UUserDto created = service.create(user);
        if(created == null) {
            log.warn("The user [{}] creation failed", user.getName());
            return new ResponseEntity<>(Message.success(ECOMMessage.CREATION_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(created, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/employee")
    public ResponseEntity<?> createEmployee(@RequestBody UUserDto user) {
        user.setUserType(EUserType.EMPLOYEE.name());
        UUserDto created = service.create(user);

        if(created == null) {
            log.warn("The user [{}] creation failed", user.getName());
            return new ResponseEntity<>(Message.success(ECOMMessage.CREATION_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(created, new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@RequestBody UUserDto prospect, @PathVariable Long id) throws RecordNotFoundException {
        UUserDto updated = service.updateById(prospect, id);
        if(updated == null) {
            log.warn("The user id: [{}] update failed", id);
            return new ResponseEntity<>(Message.success(ECOMMessage.UPDATE_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(updated, new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping("/retailer/{id}")
    public ResponseEntity<?> updateRetailerById(@RequestBody UUserDto user, @PathVariable Long id) throws RecordNotFoundException {
        user.setUserType(EUserType.RETAILER.name());
        UUserDto updated = service.updateById(user, id);
        if(updated == null) {
            log.warn("The user id: [{}] update failed", id);
            return new ResponseEntity<>(Message.success(ECOMMessage.UPDATE_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(updated, new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<?> updateEmployeeById(@RequestBody UUserDto user, @PathVariable Long id) throws RecordNotFoundException {
        user.setUserType(EUserType.EMPLOYEE.name());
        UUserDto updated = service.updateById(user, id);
        if(updated == null) {
            log.warn("The user id: [{}] update failed", id);
            return new ResponseEntity<>(Message.success(ECOMMessage.UPDATE_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(updated, new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping("/assignEmployeeRetailers/{id}")
    public ResponseEntity<?> assignEmployeeRetailers(@RequestBody List<Long> retailerIds, @PathVariable Long id) {
        UUserDto updated = service.assignEmployeeRetailers(id, retailerIds);
        if(updated == null) {
            log.warn("The user [{}] update failed", id);
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
