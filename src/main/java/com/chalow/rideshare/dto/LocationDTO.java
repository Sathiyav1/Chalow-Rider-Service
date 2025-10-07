package com.chalow.rideshare.dto;

public class LocationDTO {
    public static class ValidationRequest {
        private String address;
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }

    public static class ValidationResponse {
        private String address;
        private boolean valid;
        private String message;
        private Double latitude;
        private Double longitude;

        public ValidationResponse() {}
        public ValidationResponse(String address, boolean valid, String message, Double lat, Double lon) {
            this.address = address; this.valid = valid; this.message = message; this.latitude = lat; this.longitude = lon;
        }
        public String getAddress() { return address; }
        public boolean isValid() { return valid; }
        public String getMessage() { return message; }
        public Double getLatitude() { return latitude; }
        public Double getLongitude() { return longitude; }
        public void setAddress(String a) { this.address = a; }
        public void setValid(boolean v) { this.valid = v; }
        public void setMessage(String m) { this.message = m; }
        public void setLatitude(Double l) { this.latitude = l; }
        public void setLongitude(Double l) { this.longitude = l; }
    }
}
