package com.chalow.rideshare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class RideRequestDTO {
    public static class CreateRequest {
        @NotBlank
        private String pickupAddress;
        @NotBlank
        private String destinationAddress;

        // getters/setters
        public String getPickupAddress() { return pickupAddress; }
        public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }
        public String getDestinationAddress() { return destinationAddress; }
        public void setDestinationAddress(String destinationAddress) { this.destinationAddress = destinationAddress; }
    }

    public static class UpdateRequest {
        private String pickupAddress;
        private String destinationAddress;
        // getters/setters
        public String getPickupAddress() { return pickupAddress; }
        public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }
        public String getDestinationAddress() { return destinationAddress; }
        public void setDestinationAddress(String destinationAddress) { this.destinationAddress = destinationAddress; }
    }

    public static class Response {
        private String tripId;
        private String status;
        private String message;

        public Response() {}
        public Response(String tripId, String status, String message) {
            this.tripId = tripId; this.status = status; this.message = message;
        }
        public String getTripId() { return tripId; }
        public String getStatus() { return status; }
        public String getMessage() { return message; }
        public void setTripId(String t) { this.tripId = t; }
        public void setStatus(String s) { this.status = s; }
        public void setMessage(String m) { this.message = m; }
    }

    public static class RideHistoryResponse {
        private String tripId;
        private LocalDateTime requestedAt;
        private String pickupAddress;
        private String destinationAddress;
        // getters/setters
        public String getTripId() { return tripId; }
        public void setTripId(String tripId) { this.tripId = tripId; }
        public LocalDateTime getRequestedAt() { return requestedAt; }
        public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
        public String getPickupAddress() { return pickupAddress; }
        public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }
        public String getDestinationAddress() { return destinationAddress; }
        public void setDestinationAddress(String destinationAddress) { this.destinationAddress = destinationAddress; }
    }
}
