package com.chalow.rideshare.dto;

public class RideOfferDTO {
    public static class OfferResponse {
        private String offerId;
        private Long driverId;
        private Double price;
        public OfferResponse() {}
        public OfferResponse(String offerId, Long driverId, Double price) { this.offerId = offerId; this.driverId = driverId; this.price = price; }
        public String getOfferId() { return offerId; }
        public void setOfferId(String offerId) { this.offerId = offerId; }
        public Long getDriverId() { return driverId; }
        public void setDriverId(Long driverId) { this.driverId = driverId; }
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
    }

    public static class AcceptResponse {
        private String confirmationId;
        private String status;
        private String message;
        public AcceptResponse() {}
        public AcceptResponse(String confirmationId, String status, String message) { this.confirmationId = confirmationId; this.status = status; this.message = message; }
        public String getConfirmationId() { return confirmationId; }
        public void setConfirmationId(String confirmationId) { this.confirmationId = confirmationId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
