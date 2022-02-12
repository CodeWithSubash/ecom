package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.business.ImageBL;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.category.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BrandMapper {

    @Autowired
    ImageBL imageService;

    public PBrandDto toDto(Brand entity) {
        if(Preconditions.checkNull(entity))
            return null;

        ImageDto image = imageService.getById(entity.getId(), Brand.class.toString());
        return new PBrandDto().toBuilder()
                .id(entity.getId())
                .name(entity.getName())
                .logoPath(Preconditions.checkNotNull(image) ? image.getImagePath() : null)
                .createdBy(Preconditions.checkNotNull(entity.getCreatedUser()) ? entity.getCreatedUser().getName() : null)
                .createdAt(entity.getCreatedDate())
                .updatedBy(Preconditions.checkNotNull(entity.getUpdatedUser()) ? entity.getUpdatedUser().getName() : null)
                .updatedAt(entity.getUpdatedDate())
                .deletedAt(entity.getDeletedDate())
                .build();
    }

    public Brand toEntity(PBrandDto dto) {
        if(Preconditions.checkNull(dto))
            return null;

        return new Brand().toBuilder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }

    public List<PBrandDto> toDto(List<Brand> entityList) {
        if(Preconditions.checkNull(entityList))
            return null;

        List<PBrandDto> dtoList = new ArrayList<>();
        for (Brand entity : entityList) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }

    public PaginationRecordsDto<PBrandDto> toDto(Page<Brand> entityList) {
        if(Preconditions.checkNull(entityList))
            return null;

        List<PBrandDto> dtoList = new ArrayList<>();
        for (Brand entity : entityList.getContent()) {
            dtoList.add(toDto(entity));
        }

        return new PaginationRecordsDto<PBrandDto>().toBuilder()
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
