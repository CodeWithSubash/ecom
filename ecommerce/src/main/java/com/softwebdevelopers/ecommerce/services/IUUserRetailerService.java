package com.softwebdevelopers.ecommerce.services;

import com.softwebdevelopers.ecommerce.dto.UUserRetailerDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.user.UserRetailer;
import com.softwebdevelopers.ecommerce.models.user.UserRetailerRecord;

import java.util.List;

public interface IUUserRetailerService {

    List<UserRetailer> getAll() throws RecordNotFoundException;

    UserRetailer getById(Long id) throws RecordNotFoundException;

    UserRetailerRecord getRetailerRecord() throws RecordNotFoundException;

    UserRetailer updateRetailerById(UUserRetailerDto kyc) throws RecordNotFoundException;

    UserRetailer update(UserRetailer kyc) throws RecordNotFoundException;
}
