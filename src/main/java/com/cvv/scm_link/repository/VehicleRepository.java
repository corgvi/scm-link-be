package com.cvv.scm_link.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.cvv.scm_link.entity.Vehicle;

@Repository
public interface VehicleRepository extends BaseRepository<Vehicle, String> {
    Optional<Vehicle> findByLicensePlate(String licensePlate);

    Page<Vehicle> findAllByIsAvailableIsTrue(Pageable pageable);
}
