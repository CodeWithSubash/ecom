package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.dto.SelectItemsDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SelectItemsMapper {

    public SelectItemsDto.SelectItemIntDto toDto(Long id, String value) {
        return new SelectItemsDto.SelectItemIntDto().toBuilder()
                .id(id)
                .value(value)
                .build();
    }
}
