package com.cvv.scm_link.mapper;

import org.mapstruct.*;

import com.cvv.scm_link.dto.request.DeliveryCreateRequest;
import com.cvv.scm_link.dto.request.DeliveryUpdateRequest;
import com.cvv.scm_link.dto.response.DeliveryResponse;
import com.cvv.scm_link.entity.Delivery;

@Mapper(
        componentModel = "spring",
        uses = {DeliveryOrdersMapper.class})
public interface DeliveryMapper
        extends BaseMapper<Delivery, DeliveryCreateRequest, DeliveryUpdateRequest, DeliveryResponse> {

    @Mapping(source = "deliveryOrders", target = "deliveryOrders")
    @Mapping(source = "pickupWarehouse.latitude", target = "warehouseLatitude")
    @Mapping(source = "pickupWarehouse.longitude", target = "warehouseLongitude")
    @Override
    DeliveryResponse toDTO(Delivery entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "deliveryStatus", source = "deliveryStatus")
    @Mapping(target = "note", source = "note")
    @Override
    void updateFromDTO(DeliveryUpdateRequest dto, @MappingTarget Delivery entity);
}
