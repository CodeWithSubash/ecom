package com.softwebdevelopers.ecommerce.business;

import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.modelmapper.ProductMapper;
import com.softwebdevelopers.ecommerce.models.modelmapper.ProductStockMapper;
import com.softwebdevelopers.ecommerce.models.enums.EStockRemark;
import com.softwebdevelopers.ecommerce.models.enums.EStockType;
import com.softwebdevelopers.ecommerce.models.product.Product;
import com.softwebdevelopers.ecommerce.models.product.ProductStocks;
import com.softwebdevelopers.ecommerce.services.IPProductService;
import com.softwebdevelopers.ecommerce.services.IPProductStockService;
import com.softwebdevelopers.ecommerce.utils.ECOMUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Component
public class PProductStockBL {

    @Autowired
    IPProductStockService service;

    @Autowired
    IPProductService productService;

    @Autowired
    AuditActivityRecordBL auditService;

    @Autowired
    ProductStockMapper mapper;

    @Autowired
    ProductMapper productMapper;

    public PaginationRecordsDto<PProductStockDto> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        PaginationRecordsDto<PProductStockDto> dto = mapper.toDto(service.getAll(page));
        return dto;
    }

//    public PaginationRecordsDto<PProductStockDto> getAllByProductId(Long productId, PagingSortingAndSearchDto page) throws RecordNotFoundException {
//        PaginationRecordsDto<PProductStockDto> dto = mapper.toDto(service.getAllByProductId(productId, page));
//        return dto;
//    }

    public PaginationRecordsDto<PProductStockDto> getAllByProductId(Long productId, String startDate, String endDate, PagingSortingAndSearchDto page) throws RecordNotFoundException {
        if (Preconditions.checkNotBlank(startDate) &&
                Preconditions.checkNotBlank(endDate)) {
            PaginationRecordsDto<PProductStockDto> dto = mapper.toDto(service.getAllByProductIdAndDateRange(
                    productId,
                    ECOMUtilities.convertToLocalDateTime(startDate, null).toLocalDate().atTime(LocalTime.MIN),
                    ECOMUtilities.convertToLocalDateTime(endDate, null).toLocalDate().atTime(LocalTime.MAX),
                    page));
            return dto;
        } else {
            PaginationRecordsDto<PProductStockDto> dto = mapper.toDto(service.getAllByProductId(productId, page));
            return dto;
        }

    }

//    public PaginationRecordsDto<PProductStockDto> getAllByProductIdAndDateRange(Long productId, LocalDateTime startDate, LocalDateTime endDate, PagingSortingAndSearchDto page) throws RecordNotFoundException {
//        PaginationRecordsDto<PProductStockDto> dto = mapper.toDto(service.getAllByProductIdAndDateRange(productId, startDate, endDate, page));
//        return dto;
//    }

    public PProductStockDto create(PProductStockDto productStock) {

        Product product = productService.getById(productStock.getProductId());
        if (Preconditions.checkNotNull(product)) {
            productStock.setProduct(productMapper.toDto(product));
        } else {
            log.warn("The product [0] fetched failed. The provided product Id: [{}] is not found.", productStock.getProductId());
            throw new RecordNotFoundException("No product record exists for given product Id: [" + productStock.getProductId() + "].");
        }

        ProductStocks maxValue = service.getMaxByProductId(productStock.getProductId());

        if (Preconditions.checkNotNull(maxValue)) {
            productStock.setOldStock(maxValue.getNewStock());
            productStock.setNewStock(productStock.getChangeType().equalsIgnoreCase(EStockType.OPENING.name()) ? maxValue.getNewStock() + productStock.getChangeStock() :
                    productStock.getChangeType().equalsIgnoreCase(EStockType.INCREMENT.name()) ? maxValue.getNewStock() + productStock.getChangeStock() :
                            productStock.getChangeType().equalsIgnoreCase(EStockType.DECREMENT.name()) ? maxValue.getNewStock() - productStock.getChangeStock() : 0);
            productStock.setRemarks(Preconditions.checkNotBlank(productStock.getRemarks()) ? productStock.getRemarks() : EStockRemark.NEW_STOCK.name());
        } else {
            productStock.setChangeType(EStockType.OPENING.name());
            productStock.setOldStock(0);
            productStock.setChangeStock(productStock.getChangeStock());
            productStock.setNewStock(productStock.getChangeStock());
            productStock.setRemarks(EStockRemark.PRODUCT_CREATED.name());
        }

        ProductStocks created = service.create(mapper.toEntity(productStock));
        if (created == null) {
            log.warn("The product stock creation failed for product [{}]", productStock.getProduct().getName());
            return null;
        }

        auditService.create(
                new AuditActivityRecordDto().toBuilder()
                        .clazz(PBrandBL.class.toString())
                        .method(ECOMConstants.CREATED)
                        .asString("Added new <b>Stock</b> for <b>"
                                .concat(created.getProduct().getName())
                                .concat("</b> Product")
                                .concat(" by ")
                                .concat(created.getCreatedUser().getName()))
                        .build());

        return mapper.toDto(created);
    }

}
