package com.chalow.rideshare.dto;

public class RatingDTO {
    public static class RatingRequest {
        private int rating;
        public int getRating() { return rating; }
        public void setRating(int rating) { this.rating = rating; }
    }

    public static class RatingResponse {
        private String status;

        public RatingResponse() {}

        public RatingResponse(String status) { this.status = status; }

        public String getStatus() { return status; }

        public void setStatus(String status) { this.status = status; }
    }
}
