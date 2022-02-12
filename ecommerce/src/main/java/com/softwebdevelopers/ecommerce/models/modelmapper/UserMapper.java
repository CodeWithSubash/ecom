package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.PaginationInfoDto;
import com.softwebdevelopers.ecommerce.dto.PaginationRecordsDto;
import com.softwebdevelopers.ecommerce.dto.response.UserResponseDto;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.dto.UUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserEmployeeMapper employeeMapper;

    @Autowired
    private UserRetailerMapper retailerMapper;

    public UUserDto toDto(User entity) {
        if(Preconditions.checkNull(entity))
            return null;

        return new UUserDto().toBuilder()
                .id(entity.getId())
                .ownerUserId(Preconditions.checkNotNull(entity.getOwnerUser()) ? entity.getOwnerUser().getId() : null)
                .name(entity.getName())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .enabled(entity.isEnabled())
                .isEmailVerified(entity.isEmailVerified())
                .userType(entity.getUserType())
                .userEmployee(Preconditions.checkNotNull(entity.getUserEmployee()) ? employeeMapper.toDto(entity.getUserEmployee()) : null)
                .userRetailer(Preconditions.checkNotNull(entity.getUserRetailer()) ? retailerMapper.toDto(entity.getUserRetailer()) : null)
                .assignedRetailers(Preconditions.checkNotNull(entity.getRetailers()) ?
                        entity.getRetailers().stream().map(item ->
                                        Preconditions.checkNotNull(item.getUserRetailer()) ? retailerMapper.toDto(item.getUserRetailer()) : null
                                ).collect(Collectors.toList()) : null)
                .roles(Preconditions.checkNotNull(entity.getRoles()) ? roleMapper.toDto(entity.getRoles().stream().collect(Collectors.toList())) : null)
                .role(Preconditions.checkNotNull(entity.getRoles()) ? entity.getRoles().stream().findFirst().get().getName().substring(entity.getRoles().stream().findFirst().get().getName().indexOf("_") + 1) : null)
                .build();
    }

    public User toEntity(UUserDto dto) {
        return new User().toBuilder()
                .id(dto.getId())
                .name(dto.getName())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .enabled(dto.getEnabled())
                .isEmailVerified(dto.getIsEmailVerified())
                .build();
    }

    public List<UUserDto> toDto(List<User> userList) {
        List<UUserDto> dtoList = new ArrayList<>();
        for (User user : userList) {
            dtoList.add(toDto(user));
        }
        return dtoList;
    }

    public PaginationRecordsDto<UUserDto> toDto(Page<User> entityList) {
        List<UUserDto> dtoList = new ArrayList<>();
        for (User entity : entityList.getContent()) {
            dtoList.add(toDto(entity));
        }

        return new PaginationRecordsDto<UUserDto>().toBuilder()
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

    public UserResponseDto toResponseDto(User entity) {
        if(Preconditions.checkNull(entity))
            return null;

        return new UserResponseDto().toBuilder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .userEmployee(Preconditions.checkNotNull(entity.getUserEmployee()) ? employeeMapper.toDto(entity.getUserEmployee()) : null)
                .userRetailer(Preconditions.checkNotNull(entity.getUserRetailer()) ? retailerMapper.toDto(entity.getUserRetailer()) : null)
                .roles(Preconditions.checkNotNull(entity.getRoles()) ? roleMapper.toDto(entity.getRoles().stream().collect(Collectors.toList())) : null)
                .role(Preconditions.checkNotNull(entity.getRoles()) ? entity.getRoles().stream().findFirst().get().getName().substring(entity.getRoles().stream().findFirst().get().getName().indexOf("_") + 1) : null)
                .build();
    }
}
