package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.models.role.Role;
import com.softwebdevelopers.ecommerce.dto.RRoleDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleMapper {

    public RRoleDto toDto(Role entity) {
        return new RRoleDto().toBuilder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public Role toEntity(RRoleDto dto) {
        return new Role().toBuilder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }

    public List<RRoleDto> toDto(List<Role> roleList) {
        List<RRoleDto> dtoList = new ArrayList<>();
        for (Role user : roleList) {
            dtoList.add(toDto(user));
        }
        return dtoList;
    }

    public List<Role> toEntity(List<RRoleDto> roleList) {
        List<Role> entityList = new ArrayList<>();
        for (RRoleDto role : roleList) {
            entityList.add(toEntity(role));
        }
        return entityList;
    }
}
