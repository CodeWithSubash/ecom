package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.PCartDto;
import com.softwebdevelopers.ecommerce.models.cart.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    @Autowired
    CartDetailMapper cartDetailMapper;

    @Autowired
    UserMapper userMapper;

    public PCartDto toDto(Cart entity) {
        return new PCartDto().toBuilder()
                .id(entity.getId())
                .userName(entity.getUser().getName())
                .user(userMapper.toDto(entity.getUser()))
                .createdUser(entity.getCreatedUser().getName())
                .cartDetail(Preconditions.checkNotNull(entity.getCartDetail()) ? cartDetailMapper.toDto(entity.getCartDetail()) : null)
                .build();
    }

    public Cart toEntity(PCartDto dto) {
        return new Cart().toBuilder()
                .id(dto.getId())
                .cartDetail(Preconditions.checkNotNull(dto.getCartDetail()) ? cartDetailMapper.toEntity(dto.getCartDetail()) : null)
                .build();
    }

}
