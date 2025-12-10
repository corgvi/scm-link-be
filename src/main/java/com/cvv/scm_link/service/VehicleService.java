package com.cvv.scm_link.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cvv.scm_link.dto.filter.VehicleFilter;
import com.cvv.scm_link.dto.request.VehicleCreateRequest;
import com.cvv.scm_link.dto.request.VehicleUpdateRequest;
import com.cvv.scm_link.dto.response.VehicleResponse;
import com.cvv.scm_link.entity.Vehicle;
import com.cvv.scm_link.exception.AppException;
import com.cvv.scm_link.exception.ErrorCode;
import com.cvv.scm_link.mapper.BaseMapper;
import com.cvv.scm_link.repository.BaseRepository;
import com.cvv.scm_link.repository.VehicleRepository;
import com.cvv.scm_link.repository.specification.VehicleSpecification;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VehicleService
        extends BaseServiceImpl<VehicleCreateRequest, VehicleUpdateRequest, VehicleResponse, Vehicle, String> {
    VehicleRepository vehicleRepository;

    public VehicleService(
            BaseRepository<Vehicle, String> baseRepository,
            BaseMapper<Vehicle, VehicleCreateRequest, VehicleUpdateRequest, VehicleResponse> baseMapper,
            VehicleRepository vehicleRepository) {
        super(baseRepository, baseMapper);
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle findByLicensePlate(String licensePlate) {
        return vehicleRepository
                .findByLicensePlate(licensePlate)
                .orElseThrow(() -> new AppException(ErrorCode.LICENSE_PLATE_NOT_FOUND));
    }

    public Page<VehicleResponse> findVehiclesByStatus(Pageable pageable) {
        return vehicleRepository.findAllByIsAvailableIsTrue(pageable).map(baseMapper::toDTO);
    }

    public Page<VehicleResponse> filter(VehicleFilter filter, Pageable pageable) {
        return vehicleRepository
                .findAll(new VehicleSpecification(filter), pageable)
                .map(baseMapper::toDTO);
    }
}
