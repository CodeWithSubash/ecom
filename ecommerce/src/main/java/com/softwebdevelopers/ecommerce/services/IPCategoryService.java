package com.softwebdevelopers.ecommerce.services;

import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.category.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPCategoryService {

    List<Category> getAll(String type) throws RecordNotFoundException;

    Page<Category> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException;

    Page<Category> getAllActive(PagingSortingAndSearchDto page) throws RecordNotFoundException;

    Page<Category> getAllInactive(PagingSortingAndSearchDto page) throws RecordNotFoundException;

    Category getById(Long id) throws RecordNotFoundException;

    Category create(Category category)  throws RecordNotFoundException;

    Category update(Category category)  throws RecordNotFoundException;

    Category deleteSoft(Long id) throws RecordNotFoundException;
}
