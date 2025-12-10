package com.cvv.scm_link.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cvv.scm_link.dto.filter.SupplierFilter;
import com.cvv.scm_link.dto.request.SupplierRequest;
import com.cvv.scm_link.dto.response.SupplierResponse;
import com.cvv.scm_link.entity.Supplier;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.repository.BaseRepository;
import com.cvv.scm_link.repository.specification.SupplierSpecification;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierService
        extends BaseServiceImpl<SupplierRequest, SupplierRequest, SupplierResponse, Supplier, String> {
    public SupplierService(
            BaseRepository<Supplier, String> baseRepository,
            BaseMapper<Supplier, SupplierRequest, SupplierRequest, SupplierResponse> baseMapper) {
        super(baseRepository, baseMapper);
    }

    public Page<SupplierResponse> filter(SupplierFilter filter, Pageable pageable) {
        return baseRepository
                .findAll(new SupplierSpecification(filter), pageable)
                .map(baseMapper::toDTO);
    }
}
