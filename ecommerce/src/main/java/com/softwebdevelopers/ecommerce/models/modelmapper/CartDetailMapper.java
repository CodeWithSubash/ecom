package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.PCartDetailDto;
import com.softwebdevelopers.ecommerce.models.cart.CartDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class CartDetailMapper {

    @Autowired
    ProductMapper productMapper;

    public PCartDetailDto toDto(CartDetail entity) {
        return new PCartDetailDto().toBuilder()
                .id(entity.getId())
                .userId(entity.getCart().getUser().getId())
                .productId(Preconditions.checkNotNull(entity.getProduct()) ? entity.getProduct().getId() : null)
                .product(Preconditions.checkNotNull(entity.getProduct()) ? productMapper.toDto(entity.getProduct()) : null)
                .quantity(entity.getQuantity())
                .build();
    }

    public CartDetail toEntity(PCartDetailDto dto) {
        return new CartDetail().toBuilder()
                .id(dto.getId())
                .quantity(dto.getQuantity())
                .product(Preconditions.checkNotNull(dto.getProduct()) ? productMapper.toEntity(dto.getProduct()) : null)
                .build();
    }

    public List<PCartDetailDto> toDto(Collection<CartDetail> entityList) {
        List<PCartDetailDto> dtoList = new ArrayList<>();
        for (CartDetail entity : entityList) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }

    public List<CartDetail> toEntity(Collection<PCartDetailDto> dtoList) {
        List<CartDetail> entityList = new ArrayList<>();
        for (PCartDetailDto dto : dtoList) {
            entityList.add(toEntity(dto));
        }
        return entityList;
    }
}
