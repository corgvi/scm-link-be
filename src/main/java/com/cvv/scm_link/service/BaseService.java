package com.cvv.scm_link.service;

import com.cvv.scm_link.dto.BaseFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public interface BaseService<C, U, R, ID extends Serializable> {
    Page<R> findAll(Pageable pageable);

    Page<R> filter(BaseFilterRequest request, Pageable pageable);

    R findById(ID id);

    R create(C dto);

    R update(U dto, ID id);

    void deleteById(ID id);
}
