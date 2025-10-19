package com.cvv.scm_link.service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.cvv.scm_link.dto.BaseFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.cvv.scm_link.entity.BaseEntity;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.repository.BaseRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseServiceImpl<C, U, R, E extends BaseEntity, ID extends Serializable>
        implements BaseService<C, U, R, ID> {

    protected final BaseRepository<E, ID> baseRepository;
    protected final BaseMapper<E, C, U, R> baseMapper;

    @Transactional(readOnly = true)
    @Override
    public Page<R> findAll(Pageable pageable) {
        return baseRepository.findAll(pageable).map(baseMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<R> filter(BaseFilterRequest filter, Pageable pageable) {
        Specification<E> spec = Specification.allOf();

        if (filter.getSearch() != null && !filter.getSearch().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + filter.getSearch().toLowerCase() + "%"));
        }

        if (filter.getFromDate() != null && filter.getToDate() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.between(root.get("createdAt"), filter.getFromDate(), filter.getToDate()));
        }

        return baseRepository.findAll(spec, pageable).map(baseMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public R findById(ID id) {
        E entity = baseRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        return baseMapper.toDTO(entity);
    }

    @Transactional
    @Override
    public R create(C dto) {
        if (dto == null) throw new AppException(ErrorCode.DTO_IS_NULL);
        E entity = baseMapper.toEntity(dto);
        entity = baseRepository.save(entity);
        return baseMapper.toDTO(entity);
    }

    @Transactional
    @Override
    public R update(U dto, ID id) {
        E entity = baseRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        baseMapper.updateFromDTO(dto, entity);
        entity = baseRepository.save(entity);
        return baseMapper.toDTO(entity);
    }

    @Transactional
    @Override
    public void deleteById(ID id) {
        if (!baseRepository.existsById(id)) throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        baseRepository.deleteById(id);
    }
}
