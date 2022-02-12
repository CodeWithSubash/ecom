package com.softwebdevelopers.ecommerce.services;

import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.brand.Brand;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPBrandService {

    List<Brand> getAll() throws RecordNotFoundException;

    Page<Brand> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException;

    Page<Brand> getAllActive(PagingSortingAndSearchDto page) throws RecordNotFoundException;

    Page<Brand> getAllInactive(PagingSortingAndSearchDto page) throws RecordNotFoundException;

    Brand getById(Long id) throws RecordNotFoundException;

    Brand create(Brand brand)  throws RecordNotFoundException;

    Brand update(Brand brand)  throws RecordNotFoundException;

    Brand deleteSoft(Long id) throws RecordNotFoundException;
}
