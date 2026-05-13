package org.example.service;

import org.example.model.Location;
import org.example.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location createLocation(String name, String description, String address, String city, String postalCode, Double capacity) {
        if (locationRepository.existsByName(name)) {
            throw new IllegalArgumentException("Location with name '" + name + "' already exists");
        }

        Location location = Location.builder()
                .name(name)
                .description(description)
                .address(address)
                .city(city)
                .postalCode(postalCode)
                .capacity(capacity)
                .createdAt(LocalDateTime.now())
                .build();

        return locationRepository.save(location);
    }

    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }

    public Optional<Location> findById(Long id) {
        return locationRepository.findById(id);
    }

    public Optional<Location> findByName(String name) {
        return locationRepository.findByName(name);
    }

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    public Integer getTotalLocations() {
        return (int) locationRepository.count();
    }
}

