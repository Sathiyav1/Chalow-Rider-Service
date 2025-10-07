package com.chalow.rideshare.repository;

import com.chalow.rideshare.entity.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, Long> {
    Optional<Ride> findByConfirmationId(String confirmationId);
    Page<Ride> findByRiderIdAndActiveTrue(Long riderId, Pageable pageable);
    Page<Ride> findByRiderIdAndActiveFalse(Long riderId, Pageable pageable);
}
