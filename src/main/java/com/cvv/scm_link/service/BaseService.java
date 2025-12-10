package com.cvv.scm_link.service;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaseService<C, U, R, ID extends Serializable> {
    Page<R> findAll(Pageable pageable);

    R findById(ID id);

    R create(C dto);

    R update(U dto, ID id);

    void deleteById(ID id);
}
