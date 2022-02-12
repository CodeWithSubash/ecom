package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.business.ImageBL;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.dto.response.ProductResponseDto;
import com.softwebdevelopers.ecommerce.models.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    BrandMapper brandMapper;

    @Autowired
    TagMapper tagMapper;

    @Autowired
    ImageBL imageService;

    public PProductDto toDto(Product entity) {
        if (Preconditions.checkNull(entity))
            return null;

        List<ImageDto> images = imageService.getAllById(entity.getId(), Product.class.toString());
        return new PProductDto().toBuilder()
                .id(entity.getId())
                .categoryId(entity.getCategory().getId())
                .brandId(entity.getBrand().getId())
                .name(entity.getName())
                .slug(entity.getSlug())
                .mrp(entity.getMrp())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .shippingAndDelivery(entity.getShippingAndDelivery())
                .paymentAndReturn(entity.getPaymentAndReturn())
                .productCondition(entity.getProductCondition())
                .availableStock(entity.getAvailableStock())
                .deal(entity.isDeal())
                .tags(tagMapper.toDto(entity.getTags()))
                .files(images.stream().map(file -> new UploadFileResponseDto().toBuilder()
                                .fileName(file.getFileName())
                                .fileDownloadUri(file.getImagePath())
                                .size(file.getSize())
                                .fileType(file.getFileType())
                                .build())
                        .collect(Collectors.toList()))
                .wholesalerId(Preconditions.checkNotNull(entity.getWholesaler()) ? entity.getWholesaler().getId() : null)
                .wholesalerName(Preconditions.checkNotNull(entity.getWholesaler()) ? entity.getWholesaler().getName() : null)
                .category(Preconditions.checkNotNull(entity.getCategory()) ? categoryMapper.toDto(entity.getCategory()) : null)
                .brand(Preconditions.checkNotNull(entity.getBrand()) ? brandMapper.toDto(entity.getBrand()) : null)
                .createdBy(Preconditions.checkNotNull(entity.getCreatedUser()) ? entity.getCreatedUser().getName() : null)
                .createdAt(entity.getCreatedDate())
                .updatedBy(Preconditions.checkNotNull(entity.getUpdatedUser()) ? entity.getUpdatedUser().getName() : null)
                .updatedAt(entity.getUpdatedDate())
                .deletedAt(entity.getDeletedDate())
                .build();
    }

    public Product toEntity(PProductDto dto) {
        return new Product().toBuilder()
                .id(dto.getId())
                .name(dto.getName())
                .mrp(dto.getMrp())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .shippingAndDelivery(dto.getShippingAndDelivery())
                .paymentAndReturn(dto.getPaymentAndReturn())
                .productCondition(dto.getProductCondition())
                .availableStock(dto.getAvailableStock())
                .deal(dto.isDeal())
                .tags(tagMapper.toEntity(dto.getTags()))
                .category(Preconditions.checkNotNull(dto.getCategory()) ? categoryMapper.toEntity(dto.getCategory()): null)
                .brand(Preconditions.checkNotNull(dto.getBrand()) ? brandMapper.toEntity(dto.getBrand()): null)
                .build();
    }

    public List<PProductDto> toDto(List<Product> entityList) {
        List<PProductDto> dtoList = new ArrayList<>();
        for (Product entity : entityList) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }

    public PaginationRecordsDto<PProductDto> toDto(Page<Product> entityList) {
        if(Preconditions.checkNull(entityList)) {
            return null;
        }

        List<PProductDto> dtoList = new ArrayList<>();
        for (Product entity : entityList.getContent()) {
            dtoList.add(toDto(entity));
        }

        return new PaginationRecordsDto<PProductDto>().toBuilder()
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

    public ProductResponseDto toResponseDto(Product entity) {
        return new ProductResponseDto().toBuilder()
                .id(entity.getId())
                .categoryName(entity.getCategory().getName())
                .brandName(entity.getBrand().getName())
                .productName(entity.getName())
                .mrp(entity.getMrp())
                .price(entity.getPrice())
                .wholesalerId(Preconditions.checkNotNull(entity.getWholesaler()) ? entity.getWholesaler().getId() : null)
                .wholesalerName(Preconditions.checkNotNull(entity.getWholesaler()) ? entity.getWholesaler().getName() : null)
                .build();
    }

}
