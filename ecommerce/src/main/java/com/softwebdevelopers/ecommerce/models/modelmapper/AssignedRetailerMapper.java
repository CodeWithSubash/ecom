package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.models.product.Tag;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AssignedRetailerMapper {

    public List<Tag> toEntity(List<String> lists) {
        return lists.stream().map(tag -> new Tag().toBuilder().name(tag).build()).collect(Collectors.toList());
    }

    public List<String> toDto(Collection<Tag> lists) {
        return lists.stream().map(tag -> tag.getName()).collect(Collectors.toList());
    }
}
