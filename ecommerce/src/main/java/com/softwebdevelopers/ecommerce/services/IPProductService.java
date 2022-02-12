package com.softwebdevelopers.ecommerce.services;

import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.product.Product;
import com.softwebdevelopers.ecommerce.models.user.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPProductService {

    List<Product> getAll() throws RecordNotFoundException;

    Page<Product> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException;

    Page<Product> getAllActive(PagingSortingAndSearchDto page) throws RecordNotFoundException;

    Page<Product> getAllByPopularity(PagingSortingAndSearchDto page, Long categoryId) throws RecordNotFoundException;

    Page<Product> getAllByCategory(PagingSortingAndSearchDto page, Long categoryId) throws RecordNotFoundException;

    Product getById(Long id) throws RecordNotFoundException;

    Product create(Product product, Long id)  throws RecordNotFoundException;

    Product update(Product product, Long wholesalerId)  throws RecordNotFoundException;

    Product deleteSoft(Long id) throws RecordNotFoundException;

//    Product addToWishList(Long productId) throws RecordNotFoundException;
//
//    Product removeFromWishList(Long productId) throws RecordNotFoundException;
}
