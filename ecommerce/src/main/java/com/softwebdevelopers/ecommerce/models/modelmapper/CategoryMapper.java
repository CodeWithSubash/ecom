package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.business.ImageBL;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.models.category.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapper {

    @Autowired
    ImageBL imageService;

    public PCategoryDto toDto(Category entity) {
        if (Preconditions.checkNull(entity))
            return null;

        ImageDto image = imageService.getById(entity.getId(), Category.class.toString());
        return new PCategoryDto().toBuilder()
                .id(entity.getId())
                .name(entity.getName())
                .bannerPath(entity.getBannerPath())
                .logoPath(Preconditions.checkNotNull(image) ? image.getImagePath() : null)
                .parentId(entity.getParentId())
                .createdBy(Preconditions.checkNotNull(entity.getCreatedUser()) ? entity.getCreatedUser().getName() : null)
                .createdAt(entity.getCreatedDate())
                .updatedBy(Preconditions.checkNotNull(entity.getUpdatedUser()) ? entity.getUpdatedUser().getName() : null)
                .updatedAt(entity.getUpdatedDate())
                .deletedAt(entity.getDeletedDate())
                .build();
    }

    public Category toEntity(PCategoryDto dto) {
        if(Preconditions.checkNull(dto))
            return null;

        return new Category().toBuilder()
                .id(dto.getId())
                .name(dto.getName())
                .bannerPath(dto.getBannerPath())
                .parentId(dto.getParentId())
                .build();
    }

    public List<PCategoryDto> toDto(List<Category> entityList) {
        if(Preconditions.checkNull(entityList))
            return null;

        List<PCategoryDto> dtoList = new ArrayList<>();
        for (Category category : entityList) {
            dtoList.add(toDto(category));
        }
        return dtoList;
    }

    public PaginationRecordsDto<PCategoryDto> toDto(Page<Category> entityList) {
        if(Preconditions.checkNull(entityList))
            return null;

        List<PCategoryDto> dtoList = new ArrayList<>();
        for (Category entity : entityList.getContent()) {
            dtoList.add(toDto(entity));
        }

        return new PaginationRecordsDto<PCategoryDto>().toBuilder()
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
