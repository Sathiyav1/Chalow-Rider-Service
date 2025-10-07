package com.chalow.rideshare.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.chalow.rideshare.dto.LocationDTO;
import com.chalow.rideshare.dto.RatingDTO;
import com.chalow.rideshare.dto.RideOfferDTO;
import com.chalow.rideshare.dto.RideRequestDTO;
import com.chalow.rideshare.entity.Ride;
import com.chalow.rideshare.entity.RideRequest;
import com.chalow.rideshare.entity.User;
import com.chalow.rideshare.repository.RideOfferRepository;
import com.chalow.rideshare.repository.RideRepository;
import com.chalow.rideshare.repository.RideRequestRepository;

@Service
public class RideService {

    private final RideRequestRepository rideRequestRepository;
    private final RideOfferRepository rideOfferRepository;
    private final RideRepository rideRepository;

    public RideService(RideRequestRepository rideRequestRepository, RideOfferRepository rideOfferRepository,
            RideRepository rideRepository) {
        this.rideRequestRepository = rideRequestRepository;
        this.rideOfferRepository = rideOfferRepository;
        this.rideRepository = rideRepository;
    }

    public RideRequestDTO.Response createRideRequest(User rider, RideRequestDTO.CreateRequest request) {
        var rr = new RideRequest();
        rr.setTripId(UUID.randomUUID().toString());
        rr.setRiderId(rider.getId());
        rr.setPickupAddress(request.getPickupAddress());
        rr.setDestinationAddress(request.getDestinationAddress());
        rideRequestRepository.save(rr);
        return new RideRequestDTO.Response(rr.getTripId(), "OK", "Ride request created");
    }

    public RideRequestDTO.Response updateRideRequest(User rider, String tripId, RideRequestDTO.UpdateRequest request) {
        var rr = rideRequestRepository.findByTripId(tripId)
                .orElseThrow(() -> new IllegalArgumentException("not found"));
        if (!rr.getRiderId().equals(rider.getId()))
            throw new IllegalArgumentException("not found");
        rr.setPickupAddress(request.getPickupAddress());
        rr.setDestinationAddress(request.getDestinationAddress());
        rideRequestRepository.save(rr);
        return new RideRequestDTO.Response(tripId, "OK", "Ride request updated");
    }

    public RideRequestDTO.Response cancelRideRequest(User rider, String tripId) {
        var rr = rideRequestRepository.findByTripId(tripId)
                .orElseThrow(() -> new IllegalArgumentException("not found"));
        if (!rr.getRiderId().equals(rider.getId()))
            throw new IllegalArgumentException("not found");
        rr.setActive(false);
        rideRequestRepository.save(rr);
        return new RideRequestDTO.Response(tripId, "OK", "Ride request cancelled");
    }

    public RatingDTO.RatingResponse rateDriver(User rider, String confirmationId, RatingDTO.RatingRequest request) {
        var ride = rideRepository.findByConfirmationId(confirmationId)
                .orElseThrow(() -> new IllegalArgumentException("not found"));
        if (!ride.getRiderId().equals(rider.getId()))
            throw new IllegalArgumentException("not found");
        // persist rating if needed; here we return an empty success response
        return new RatingDTO.RatingResponse();
    }

    public Page<RideRequestDTO.RideHistoryResponse> getCurrentRides(User rider, int page, int size) {
        var pr = rideRequestRepository.findByRiderIdAndActiveTrue(rider.getId(), PageRequest.of(page, size));
        var content = pr.getContent().stream().map(r -> {
            var resp = new RideRequestDTO.RideHistoryResponse();
            resp.setTripId(r.getTripId());
            if (r.getCreatedAt() != null)
                resp.setRequestedAt(LocalDateTime.ofInstant(r.getCreatedAt(), ZoneId.systemDefault()));
            resp.setPickupAddress(r.getPickupAddress());
            resp.setDestinationAddress(r.getDestinationAddress());
            return resp;
        }).collect(Collectors.toList());
        return new PageImpl<>(content, pr.getPageable(), pr.getTotalElements());
    }

    public Page<RideRequestDTO.RideHistoryResponse> getRideHistory(User rider, int page, int size) {
        var pr = rideRequestRepository.findByRiderIdAndActiveFalse(rider.getId(), PageRequest.of(page, size));
        var content = pr.getContent().stream().map(r -> {
            var resp = new RideRequestDTO.RideHistoryResponse();
            resp.setTripId(r.getTripId());
            if (r.getCreatedAt() != null)
                resp.setRequestedAt(LocalDateTime.ofInstant(r.getCreatedAt(), ZoneId.systemDefault()));
            resp.setPickupAddress(r.getPickupAddress());
            resp.setDestinationAddress(r.getDestinationAddress());
            return resp;
        }).collect(Collectors.toList());
        return new PageImpl<>(content, pr.getPageable(), pr.getTotalElements());
    }

    public Page<RideOfferDTO.OfferResponse> getRideOffers(User rider, String tripId, int page, int size) {
        var pr = rideOfferRepository.findByRideRequestId(tripId, PageRequest.of(page, size));
        var content = pr.getContent().stream()
                .map(o -> new RideOfferDTO.OfferResponse(o.getOfferId(), o.getDriverId(), o.getAmount()))
                .collect(Collectors.toList());
        return new PageImpl<>(content, pr.getPageable(), pr.getTotalElements());
    }

    public RideOfferDTO.AcceptResponse acceptRideOffer(User rider, String tripId, String offerId) {
        var ro = rideOfferRepository.findByOfferId(offerId)
                .orElseThrow(() -> new IllegalArgumentException("not found"));
        if (!ro.getRideRequestId().equals(tripId))
            throw new IllegalArgumentException("not found");
        var ride = new Ride();
        ride.setConfirmationId(UUID.randomUUID().toString());
        ride.setRiderId(rider.getId());
        ride.setDriverId(ro.getDriverId());
        rideRepository.save(ride);
        return new RideOfferDTO.AcceptResponse(ride.getConfirmationId(), "OK", "Offer accepted");
    }

    public LocationDTO.ValidationResponse validateAndGeocodeAddress(LocationDTO.ValidationRequest request) {
        // fake geocode
        return new LocationDTO.ValidationResponse(request.getAddress(), true, "OK", 0.0, 0.0);
    }
}
