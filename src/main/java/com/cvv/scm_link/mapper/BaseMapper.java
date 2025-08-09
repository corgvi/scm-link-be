package com.cvv.scm_link.mapper;

import java.util.List;

import org.mapstruct.MappingTarget;

import com.cvv.scm_link.entity.BaseEntity;

public interface BaseMapper<E extends BaseEntity, C, U, R> {
    R toDTO(E entity);

    E toEntity(C dto);

    void updateFromDTO(U dto, @MappingTarget E entity);

    List<R> toDTOList(List<E> list);
}
