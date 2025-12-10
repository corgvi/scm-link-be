package com.cvv.scm_link.mapper;

import org.mapstruct.Mapper;

import com.cvv.scm_link.dto.request.SupplierRequest;
import com.cvv.scm_link.dto.response.SupplierResponse;
import com.cvv.scm_link.entity.Supplier;

@Mapper(componentModel = "spring")
public interface SupplierMapper extends BaseMapper<Supplier, SupplierRequest, SupplierRequest, SupplierResponse> {}
