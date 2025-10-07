package com.chalow.rideshare.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chalow.rideshare.dto.LocationDTO;
import com.chalow.rideshare.dto.PageResponse;
import com.chalow.rideshare.dto.RatingDTO;
import com.chalow.rideshare.dto.RideOfferDTO;
import com.chalow.rideshare.dto.RideRequestDTO;
import com.chalow.rideshare.service.RideService;
import com.chalow.rideshare.service.UserService;
import com.chalow.rideshare.util.InputSanitizer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/rides")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Rider API", description = "Endpoints for ride requests and rider operations")
public class RiderController {

    public static final String ERROR = "ERROR";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";
    private static final Logger LOG = LoggerFactory.getLogger(RiderController.class);
    private final RideService rideService;
    private final UserService userService;

    public RiderController(RideService rideService, UserService userService) {
        this.rideService = rideService;
        this.userService = userService;
    }

    @PostMapping("/requests")
    @Operation(summary = "Create a new ride request", description = "Submit a new ride request with pickup and destination details")
    public ResponseEntity<RideRequestDTO.Response> createRideRequest(
            @Valid @RequestBody RideRequestDTO.CreateRequest request, Authentication authentication) {
        try {
            LOG.info("Creating ride request for user: {}", authentication.getName());
            var riderDto = userService.getCurrentUserOrCreate(authentication);
            var rider = userService.getUserEntityById(riderDto.getId());
            if (!userService.canUserRequestRide(rider)) {
                var errorResponse = new RideRequestDTO.Response(null, "UNAUTHORIZED",
                        "User is not authorized to request rides");
                return ResponseEntity.status(403).body(errorResponse);
            }
            var response = rideService.createRideRequest(rider, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            LOG.warn("Invalid ride request for user: {}: {}", authentication.getName(), e.getMessage());
            var errorResponse = new RideRequestDTO.Response(null, "INVALID_REQUEST",
                    InputSanitizer.sanitizeMessage(e.getMessage()));
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            LOG.error("Error creating ride request for user: {}", authentication.getName(), e);
            var errorResponse = new RideRequestDTO.Response(null, ERROR, INTERNAL_SERVER_ERROR);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PutMapping("/requests/{tripId}")
    public ResponseEntity<RideRequestDTO.Response> updateRideRequest(
            @Parameter(description = "Trip ID of the ride request to update") @PathVariable String tripId,
            @Valid @RequestBody RideRequestDTO.UpdateRequest request, Authentication authentication) {
        try {
            LOG.info("Updating ride request {} for user: {}", InputSanitizer.sanitizeAndTrim(tripId),
                    authentication.getName());
            var riderDto = userService.getCurrentUserOrCreate(authentication);
            var rider = userService.getUserEntityById(riderDto.getId());
            var response = rideService.updateRideRequest(rider, tripId, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            LOG.warn("Invalid update request for ride {} by user: {}: {}", InputSanitizer.sanitizeAndTrim(tripId),
                    authentication.getName(), e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOG.error("Error updating ride request {} for user: {}", InputSanitizer.sanitizeAndTrim(tripId),
                    authentication.getName(), e);
            var errorResponse = new RideRequestDTO.Response(InputSanitizer.sanitizeAndTrim(tripId), ERROR,
                    INTERNAL_SERVER_ERROR);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PatchMapping("/requests/{tripId}/cancel")
    public ResponseEntity<RideRequestDTO.Response> cancelRideRequest(
            @Parameter(description = "Trip ID of the ride request to cancel") @PathVariable String tripId,
            Authentication authentication) {
        try {
            LOG.info("Cancelling ride request {} for user: {}", InputSanitizer.sanitizeAndTrim(tripId),
                    authentication.getName());
            var riderDto = userService.getCurrentUserOrCreate(authentication);
            var rider = userService.getUserEntityById(riderDto.getId());
            var response = rideService.cancelRideRequest(rider, tripId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            LOG.warn("Cannot cancel ride {} for user: {}: {}", InputSanitizer.sanitizeAndTrim(tripId),
                    authentication.getName(), e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOG.error("Error cancelling ride request {} for user: {}", InputSanitizer.sanitizeAndTrim(tripId),
                    authentication.getName(), e);
            var errorResponse = new RideRequestDTO.Response(InputSanitizer.sanitizeAndTrim(tripId), ERROR,
                    INTERNAL_SERVER_ERROR);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("/rate/driver/{confirmationId}")
    public ResponseEntity<RatingDTO.RatingResponse> rateDriver(
            @Parameter(description = "Confirmation ID of the completed ride") @PathVariable String confirmationId,
            @Valid @RequestBody RatingDTO.RatingRequest request, Authentication authentication) {
        try {
            LOG.info("Rating driver for ride {} by user: {}", InputSanitizer.sanitizeAndTrim(confirmationId),
                    authentication.getName());
            var riderDto = userService.getCurrentUserOrCreate(authentication);
            var rider = userService.getUserEntityById(riderDto.getId());
            var response = rideService.rateDriver(rider, confirmationId, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            LOG.warn("Cannot rate driver for ride {} by user: {}: {}", InputSanitizer.sanitizeAndTrim(confirmationId),
                    authentication.getName(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            LOG.error("Error rating driver for ride {} by user: {}", InputSanitizer.sanitizeAndTrim(confirmationId),
                    authentication.getName(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/requests/current")
    public ResponseEntity<PageResponse<RideRequestDTO.RideHistoryResponse>> getCurrentRides(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        try {
            LOG.info("Getting current rides for user: {} (page: {}, size: {})", authentication.getName(), page, size);
            var riderDto = userService.getCurrentUserOrCreate(authentication);
            var rider = userService.getUserEntityById(riderDto.getId());
            int validatedSize = Math.max(1, Math.min(size, 100));
            int validatedPage = Math.max(0, page);
            com.chalow.rideshare.entity.User riderEntity = userService.getUserEntityById(rider.getId());
            var response = rideService.getCurrentRides(riderEntity, validatedPage, validatedSize);
            return ResponseEntity.ok(new PageResponse<>(response.getContent(), response));
        } catch (Exception e) {
            LOG.error("Error getting current rides for user: {}", authentication.getName(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/requests/history")
    public ResponseEntity<PageResponse<RideRequestDTO.RideHistoryResponse>> getRideHistory(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        try {
            LOG.info("Getting ride history for user: {} (page: {}, size: {})", authentication.getName(), page, size);
            var riderDto = userService.getCurrentUserOrCreate(authentication);
            var rider = userService.getUserEntityById(riderDto.getId());
            int validatedSize = Math.max(1, Math.min(size, 100));
            int validatedPage = Math.max(0, page);
            com.chalow.rideshare.entity.User riderEntity = userService.getUserEntityById(rider.getId());
            var response = rideService.getRideHistory(riderEntity, validatedPage, validatedSize);
            return ResponseEntity.ok(new PageResponse<>(response.getContent(), response));
        } catch (Exception e) {
            LOG.error("Error getting ride history for user: {}", authentication.getName(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/address/validate")
    public ResponseEntity<LocationDTO.ValidationResponse> validateAddress(@RequestBody String addressString,
            Authentication authentication) {
        try {
            LOG.info("Validating address for user: {}", authentication.getName());
            if (addressString == null || addressString.isBlank()) {
                var errorResponse = new LocationDTO.ValidationResponse(InputSanitizer.sanitizeAddress(addressString),
                        false, "Address is required", null, null);
                return ResponseEntity.badRequest().body(errorResponse);
            }
            var request = new LocationDTO.ValidationRequest();
            request.setAddress(InputSanitizer.sanitizeAddress(addressString.trim()));
            var riderDto = userService.getCurrentUserOrCreate(authentication);
            var rider = userService.getUserEntityById(riderDto.getId());
            var response = rideService.validateAndGeocodeAddress(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOG.error("Error validating address for user: {}", authentication.getName(), e);
            var errorResponse = new LocationDTO.ValidationResponse(InputSanitizer.sanitizeAddress(addressString), false,
                    "Address validation failed", null, null);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/requests/{tripId}/offers")
    public ResponseEntity<PageResponse<RideOfferDTO.OfferResponse>> getRideOffers(
            @Parameter(description = "Trip ID of the ride request") @PathVariable String tripId,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        try {
            LOG.info("Getting offers for ride request {} by user: {}", InputSanitizer.sanitizeAndTrim(tripId),
                    authentication.getName());
            var riderDto = userService.getCurrentUserOrCreate(authentication);
            var rider = userService.getUserEntityById(riderDto.getId());
            int validatedSize = Math.max(1, Math.min(size, 100));
            int validatedPage = Math.max(0, page);
            var response = rideService.getRideOffers(rider, tripId, validatedPage, validatedSize);
            return ResponseEntity.ok(new PageResponse<>(response.getContent(), response));
        } catch (IllegalArgumentException e) {
            LOG.warn("Ride request {} not found for user: {}: {}", InputSanitizer.sanitizeAndTrim(tripId),
                    authentication.getName(), e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOG.error("Error getting offers for ride request {} by user: {}", InputSanitizer.sanitizeAndTrim(tripId),
                    authentication.getName(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/requests/{tripId}/offers/{offerId}/accept")
    public ResponseEntity<RideOfferDTO.AcceptResponse> acceptRideOffer(@PathVariable String tripId,
            @PathVariable String offerId, Authentication authentication) {
        try {
            LOG.info("Accepting offer {} for ride request {} by user: {}", InputSanitizer.sanitizeAndTrim(offerId),
                    InputSanitizer.sanitizeAndTrim(tripId), authentication.getName());
            var riderDto = userService.getCurrentUserOrCreate(authentication);
            var rider = userService.getUserEntityById(riderDto.getId());
            var response = rideService.acceptRideOffer(rider, tripId, offerId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            LOG.warn("Cannot accept offer {} for ride request {} by user: {}: {}",
                    InputSanitizer.sanitizeAndTrim(offerId), InputSanitizer.sanitizeAndTrim(tripId),
                    authentication.getName(), e.getMessage());
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            LOG.error("Error accepting offer {} for ride request {} by user: {}",
                    InputSanitizer.sanitizeAndTrim(offerId), InputSanitizer.sanitizeAndTrim(tripId),
                    authentication.getName(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
