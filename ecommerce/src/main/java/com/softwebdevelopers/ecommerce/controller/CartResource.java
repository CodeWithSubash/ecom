package com.softwebdevelopers.ecommerce.controller;

import com.softwebdevelopers.ecommerce.business.PCartBL;
import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Message;
import com.softwebdevelopers.ecommerce.dto.PCartDetailDto;
import com.softwebdevelopers.ecommerce.dto.PCartDto;
import com.softwebdevelopers.ecommerce.dto.PCategoryDto;
import com.softwebdevelopers.ecommerce.dto.PProductDto;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(WebSecurityConfig.BASE_URL + CartResource.AUTHENTICATED_PATH)
public class CartResource {

    public static final String AUTHENTICATED_PATH = "/auth/cart";

    @Autowired
    PCartBL service;

    @GetMapping("/getByUser")
    public ResponseEntity<PCartDto> getByUser() throws RecordNotFoundException {
        PCartDto entity = service.getAllByUser();
        return new ResponseEntity<>(entity, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/getByUser/{retailerId}")
    public ResponseEntity<PCartDto> getByUser(@PathVariable Long retailerId) throws RecordNotFoundException {
        PCartDto entity = service.getAllByCreatedUser(retailerId);
        return new ResponseEntity<>(entity, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/addToCart")
    public ResponseEntity<?> addToCart(@RequestBody PCartDetailDto cart) throws IOException, FileUploadException {

        PCartDetailDto created = service.create(cart);
        if (created == null) {
            log.warn("The cart creation failed.");
            return new ResponseEntity<>(Message.success(ECOMMessage.CREATION_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(created, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/{cartDetailId}")
    public ResponseEntity<?> deleteById(@PathVariable Long cartDetailId) throws RecordNotFoundException {
        try {
            service.deleteById(cartDetailId);
            return new ResponseEntity<>(Message.success(ECOMMessage.RECORD_DELETED), new HttpHeaders(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(Message.warn(ECOMMessage.RECORD_DOES_NOT_EXIST), new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
