-- V1 initial schema for RiderService
CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE,
  display_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS ride_requests (
  id SERIAL PRIMARY KEY,
  trip_id VARCHAR(255) UNIQUE,
  rider_id BIGINT REFERENCES users(id),
  pickup_address TEXT,
  destination_address TEXT,
  active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS ride_offers (
  id SERIAL PRIMARY KEY,
  offer_id VARCHAR(255) UNIQUE,
  ride_request_id VARCHAR(255),
  driver_id BIGINT,
  amount NUMERIC,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS rides (
  id SERIAL PRIMARY KEY,
  confirmation_id VARCHAR(255) UNIQUE,
  rider_id BIGINT REFERENCES users(id),
  driver_id BIGINT,
  active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);
