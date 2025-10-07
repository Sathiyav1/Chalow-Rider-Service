package com.chalow.rideshare.repository;

import com.chalow.rideshare.entity.RideOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RideOfferRepository extends JpaRepository<RideOffer, Long> {
    Optional<RideOffer> findByOfferId(String offerId);
    Page<RideOffer> findByRideRequestId(String tripId, Pageable pageable);
    List<RideOffer> findByRideRequestId(String tripId);
}
