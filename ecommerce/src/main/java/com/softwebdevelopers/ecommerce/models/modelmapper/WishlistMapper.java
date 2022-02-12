package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.PWishlistDto;
import com.softwebdevelopers.ecommerce.models.cart.Wishlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WishlistMapper {

    @Autowired
    ProductMapper productMapper;

    public PWishlistDto toDto(Wishlist entity) {
        return new PWishlistDto().toBuilder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .name(entity.getUser().getName())
                .product(Preconditions.checkNotNull(entity.getProduct()) ? productMapper.toDto(entity.getProduct()) : null)
                .createdAt(entity.getCreatedDate())
                .deletedAt(entity.getDeletedDate())
                .build();
    }

    public List<PWishlistDto> toDto(List<Wishlist> entityList) {
        List<PWishlistDto> dtoList = new ArrayList<>();
        for (Wishlist entity : entityList) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }
}
