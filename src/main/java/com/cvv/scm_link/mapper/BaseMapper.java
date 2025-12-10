package com.cvv.scm_link.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.cvv.scm_link.entity.BaseEntity;

public interface BaseMapper<E extends BaseEntity, C, U, R> {
    R toDTO(E entity);

    E toEntity(C dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDTO(U dto, @MappingTarget E entity);

    List<R> toDTOList(List<E> list);
}
