package com.cvv.scm_link.mapper;

import org.mapstruct.*;

import com.cvv.scm_link.dto.request.VehicleCreateRequest;
import com.cvv.scm_link.dto.request.VehicleUpdateRequest;
import com.cvv.scm_link.dto.response.VehicleResponse;
import com.cvv.scm_link.entity.Vehicle;

@Mapper(componentModel = "spring")
public interface VehicleMapper
        extends BaseMapper<Vehicle, VehicleCreateRequest, VehicleUpdateRequest, VehicleResponse> {
    @Mapping(source = "available", target = "available")
    @Override
    VehicleResponse toDTO(Vehicle entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "available", target = "available")
    @Override
    void updateFromDTO(VehicleUpdateRequest dto, @MappingTarget Vehicle entity);
}
