package com.chalow.rideshare.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "ride_offers")
public class RideOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "offer_id", unique = true)
    private String offerId;

    @Column(name = "ride_request_id")
    private String rideRequestId;

    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOfferId() { return offerId; }
    public void setOfferId(String offerId) { this.offerId = offerId; }
    public String getRideRequestId() { return rideRequestId; }
    public void setRideRequestId(String rideRequestId) { this.rideRequestId = rideRequestId; }
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public Instant getCreatedAt() { return createdAt; }

}
