package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.PProductStockDto;
import com.softwebdevelopers.ecommerce.dto.PaginationInfoDto;
import com.softwebdevelopers.ecommerce.dto.PaginationRecordsDto;
import com.softwebdevelopers.ecommerce.models.enums.EStockRemark;
import com.softwebdevelopers.ecommerce.models.enums.EStockType;
import com.softwebdevelopers.ecommerce.models.product.ProductStocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductStockMapper {

    @Autowired
    ProductMapper productMapper;

    public PProductStockDto toDto(ProductStocks entity) {
        if(Preconditions.checkNull(entity)) return null;
        return new PProductStockDto().toBuilder()
                .id(entity.getId())
                .product(productMapper.toDto(entity.getProduct()))
                .oldStock(entity.getOldStock())
                .changeStock(entity.getChangeStock())
                .newStock(entity.getNewStock())
                .changeType(entity.getChangeType().name())
                .remarks(entity.getRemarks().name())
                .createdUser(Preconditions.checkNotNull(entity.getCreatedUser()) ? entity.getCreatedUser().getName() : null)
                .updatedUser(Preconditions.checkNotNull(entity.getUpdatedUser()) ? entity.getUpdatedUser().getName() : null)
                .createdAt(entity.getCreatedDate())
                .build();
    }

    public ProductStocks toEntity(PProductStockDto dto) {
        if(Preconditions.checkNull(dto)) return null;
        return new ProductStocks().toBuilder()
                .id(dto.getId())
                .product(productMapper.toEntity(dto.getProduct()))
                .oldStock(dto.getOldStock())
                .changeStock(dto.getChangeStock())
                .newStock(dto.getNewStock())
                .changeType(EStockType.valueOf(dto.getChangeType()))
                .remarks(EStockRemark.valueOf(dto.getRemarks()))
                .build();
    }

    public List<PProductStockDto> toDto(List<ProductStocks> stockList) {
        if(Preconditions.checkNull(stockList)) return null;
        List<PProductStockDto> dtoList = new ArrayList<>();
        for (ProductStocks stock : stockList) {
            dtoList.add(toDto(stock));
        }
        return dtoList;
    }

    public List<ProductStocks> toEntity(List<PProductStockDto> stockList) {
        if(Preconditions.checkNull(stockList)) return null;

        List<ProductStocks> dtoList = new ArrayList<>();
        for (PProductStockDto dto : stockList) {
            dtoList.add(toEntity(dto));
        }
        return dtoList;
    }

    public PaginationRecordsDto<PProductStockDto> toDto(Page<ProductStocks> entityList) {

        if(Preconditions.checkNull(entityList)) return null;

        List<PProductStockDto> dtoList = new ArrayList<>();
        for (ProductStocks entity : entityList.getContent()) {
            dtoList.add(toDto(entity));
        }

        return new PaginationRecordsDto<PProductStockDto>().toBuilder()
                .paginationInfo(new PaginationInfoDto().toBuilder()
                        .currentPage(entityList.getNumber())
                        .totalItems(entityList.getTotalElements())
                        .totalPages(entityList.getTotalPages())
                        .size(entityList.getSize())
                        .numberOfItems(entityList.getNumberOfElements())
                        .isFirst(entityList.isFirst())
                        .isLast(entityList.isLast())
                        .hasNext(entityList.hasNext())
                        .hasPrevious(entityList.hasPrevious())
                        .build())
                .data(dtoList)
                .build();

    }
}
