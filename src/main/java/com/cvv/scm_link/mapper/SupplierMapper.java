package com.cvv.scm_link.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.cvv.scm_link.dto.request.SupplierRequest;
import com.cvv.scm_link.dto.response.SupplierResponse;
import com.cvv.scm_link.entity.Supplier;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface SupplierMapper extends BaseMapper<Supplier, SupplierRequest, SupplierRequest, SupplierResponse> {
    @Override
    @Mapping(target = "isActive", source = "active")
    SupplierResponse toDTO(Supplier entity);
}
