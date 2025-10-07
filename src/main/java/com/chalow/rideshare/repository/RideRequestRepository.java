package com.chalow.rideshare.repository;

import com.chalow.rideshare.entity.RideRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RideRequestRepository extends JpaRepository<RideRequest, Long> {
    Optional<RideRequest> findByTripId(String tripId);
    Page<RideRequest> findByRiderIdAndActiveTrue(Long riderId, Pageable pageable);
    Page<RideRequest> findByRiderIdAndActiveFalse(Long riderId, Pageable pageable);
}
