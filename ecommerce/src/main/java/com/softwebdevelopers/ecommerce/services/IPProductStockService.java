package com.softwebdevelopers.ecommerce.services;

import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.product.ProductStocks;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface IPProductStockService {

    Page<ProductStocks> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException;

    Page<ProductStocks> getAllByProductId(Long product_id, PagingSortingAndSearchDto page) throws RecordNotFoundException;

    Page<ProductStocks> getAllByProductIdAndDateRange(Long product_id, LocalDateTime startDate, LocalDateTime endDate, PagingSortingAndSearchDto page) throws RecordNotFoundException;

    ProductStocks getById(Long id) throws RecordNotFoundException;

    ProductStocks getMaxByProductId(Long productId) throws RecordNotFoundException;

    ProductStocks create(ProductStocks productStocks)  throws RecordNotFoundException;

    ProductStocks deleteSoft(Long id) throws RecordNotFoundException;
}
