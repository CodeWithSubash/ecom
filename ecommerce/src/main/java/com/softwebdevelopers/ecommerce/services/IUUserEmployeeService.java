package com.softwebdevelopers.ecommerce.services;

import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.dto.UUserEmployeeRecordDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.user.UserEmployee;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IUUserEmployeeService {

    Page<UserEmployee> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException;

    UserEmployee getById(Long id) throws RecordNotFoundException;

    Object getEmployeeRecord() throws RecordNotFoundException;

    UserEmployee create(UserEmployee user)  throws RecordNotFoundException;

    UserEmployee update(UserEmployee user, String role) throws RecordNotFoundException;

    UserEmployee deleteSoft(Long id) throws RecordNotFoundException;
}
