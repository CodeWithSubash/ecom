package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.FeaturedImageDto;
import com.softwebdevelopers.ecommerce.dto.PProductDto;
import com.softwebdevelopers.ecommerce.dto.PaginationInfoDto;
import com.softwebdevelopers.ecommerce.dto.PaginationRecordsDto;
import com.softwebdevelopers.ecommerce.models.common.FeaturedImage;
import com.softwebdevelopers.ecommerce.models.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class FeaturedImageMapper {

    @Autowired
    CategoryMapper mapper;

    public FeaturedImageDto toDto(FeaturedImage entity) {
        if(Preconditions.checkNull(entity))
            return null;

        return new FeaturedImageDto().toBuilder()
                .id(entity.getId())
                .categoryId(Preconditions.checkNotNull(entity.getCategory()) ? entity.getCategory().getId() : null)
                .title(entity.getTitle())
                .subTitle(entity.getSubTitle())
                .description(entity.getDescription())
                .featuredType(entity.getFeaturedType())
                .category(Preconditions.checkNotNull(entity.getCategory()) ? mapper.toDto(entity.getCategory()) : null)
                .createdAt(entity.getCreatedDate())
                .deletedAt(entity.getDeletedDate())
                .build();
    }

    public FeaturedImage toEntity(FeaturedImageDto dto) {
        if(Preconditions.checkNull(dto))
            return null;

        return new FeaturedImage().toBuilder()
                .id(dto.getId())
                .title(dto.getTitle())
                .subTitle(dto.getSubTitle())
                .description(dto.getDescription())
                .featuredType(dto.getFeaturedType())
                .category(Preconditions.checkNotNull(dto.getCategory()) ?
                        mapper.toEntity(dto.getCategory()) : null)
                .deletedDate(LocalDateTime.now())
                .build();
    }

    public List<FeaturedImageDto> toDto(List<FeaturedImage> entityList) {
        List<FeaturedImageDto> dtoList = new ArrayList<>();
        for (FeaturedImage entity : entityList) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }

    public PaginationRecordsDto<FeaturedImageDto> toDto(Page<FeaturedImage> entityList) {
        List<FeaturedImageDto> dtoList = new ArrayList<>();
        for (FeaturedImage entity : entityList.getContent()) {
            dtoList.add(toDto(entity));
        }

        return new PaginationRecordsDto<FeaturedImageDto>().toBuilder()
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
