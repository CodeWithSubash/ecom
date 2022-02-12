package com.softwebdevelopers.ecommerce.controller;

import com.softwebdevelopers.ecommerce.business.PWishlistBL;
import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Message;
import com.softwebdevelopers.ecommerce.dto.PWishlistDto;
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
@RequestMapping(WebSecurityConfig.BASE_URL + WishlistResource.AUTHENTICATED_PATH)
public class WishlistResource {

    public static final String AUTHENTICATED_PATH = "/auth/wishlist";

    @Autowired
    PWishlistBL service;

    @GetMapping("/getByUser")
    public ResponseEntity<List<PWishlistDto>> getByUser() throws RecordNotFoundException {
        List<PWishlistDto> entity = service.getAllByUser();
        return new ResponseEntity<>(entity, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<?> createWishlist(@PathVariable Long productId) throws IOException, FileUploadException {

        PWishlistDto created = service.create(productId);
        if (created == null) {
            log.warn("The wishlist creation failed.");
            return new ResponseEntity<>(Message.success(ECOMMessage.CREATION_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(created, new HttpHeaders(), HttpStatus.OK);
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

    @DeleteMapping("/deleteByProduct/{productId}")
    public ResponseEntity<?> deleteByProductId(@PathVariable Long productId) throws RecordNotFoundException {
        try {
            service.deleteByProductId(productId);
            return new ResponseEntity<>(Message.success(ECOMMessage.RECORD_DELETED), new HttpHeaders(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(Message.warn(ECOMMessage.RECORD_DOES_NOT_EXIST), new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
