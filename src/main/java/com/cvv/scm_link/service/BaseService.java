package com.cvv.scm_link.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseService<C, U, R, ID extends Serializable> {
    List<R> findAll();

    Optional<R> findById(ID id);

    R create(C dto);

    R update(U dto, ID id);

    void deleteById(ID id);
}
