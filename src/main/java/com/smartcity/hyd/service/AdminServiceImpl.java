package com.smartcity.hyd.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.smartcity.hyd.model.*;
import com.smartcity.hyd.repo.*;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired private HotelRepo hotelRepo;
    @Autowired private RestaurantRepo restaurantRepo;
    @Autowired private PgRepo pgRepo;
    @Autowired private TouristPlaceRepo touristRepo;
    @Autowired private BusinessVenueRepo businessRepo;
    @Autowired private CollegeRepo collegeRepo;

    // ----- counts -----
    @Override
    public long countHotels() { return hotelRepo.count(); }
    @Override
    public long countRestaurants() { return restaurantRepo.count(); }
    @Override
    public long countPgs() { return pgRepo.count(); }
    @Override
    public long countTouristPlaces() { return touristRepo.count(); }
    @Override
    public long countBusinessVenues() { return businessRepo.count(); }
    @Override
    public long countColleges() { return collegeRepo.count(); }

    // ----- hotels -----
    @Override
    public Page<Hotel> searchHotels(String q, Pageable pageable) {
        if (q == null || q.isBlank()) return hotelRepo.findAll(pageable);
        return hotelRepo.findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(q, q, pageable);
    }
    @Override public Hotel saveHotel(Hotel hotel) { return hotelRepo.save(hotel); }
    @Override public Optional<Hotel> findHotelById(Long id) { return hotelRepo.findById(id); }
    @Override public void deleteHotel(Long id) { hotelRepo.deleteById(id); }

    // ----- restaurants -----
    @Override
    public Page<Restaurant> searchRestaurants(String q, Pageable pageable) {
        if (q == null || q.isBlank()) return restaurantRepo.findAll(pageable);
        return restaurantRepo.findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(q, q, pageable);
    }
    @Override public Restaurant saveRestaurant(Restaurant r) { return restaurantRepo.save(r); }
    @Override public Optional<Restaurant> findRestaurantById(Long id) { return restaurantRepo.findById(id); }
    @Override public void deleteRestaurant(Long id) { restaurantRepo.deleteById(id); }

    // ----- pgs -----
    @Override
    public Page<PgAccommodation> searchPgAccommodations(String q, Pageable pageable) {
        if (q == null || q.isBlank()) return pgRepo.findAll(pageable);
        return pgRepo.findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(q, q, pageable);
    }
    @Override public PgAccommodation savePgAccommodation(PgAccommodation p) { return pgRepo.save(p); }
    @Override public Optional<PgAccommodation> findPgAccommodationById(Long id) { return pgRepo.findById(id); }
    @Override public void deletePgAccommodation(Long id) { pgRepo.deleteById(id); }

    // ----- tourist places -----
    @Override
    public Page<TouristPlace> searchTouristPlaces(String q, Pageable pageable) {
        if (q == null || q.isBlank()) return touristRepo.findAll(pageable);
        return touristRepo.findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(q, q, pageable);
    }
    @Override public TouristPlace saveTouristPlace(TouristPlace t) { return touristRepo.save(t); }
    @Override public Optional<TouristPlace> findTouristPlaceById(Long id) { return touristRepo.findById(id); }
    @Override public void deleteTouristPlace(Long id) { touristRepo.deleteById(id); }

    // ----- business venues -----
    @Override
    public Page<BusinessVenue> searchBusinessVenues(String q, Pageable pageable) {
        if (q == null || q.isBlank()) return businessRepo.findAll(pageable);
        return businessRepo.findByNameContainingIgnoreCaseOrBusinessTypeContainingIgnoreCaseOrLocationContainingIgnoreCase(q, q, q, pageable);
    }
    @Override public BusinessVenue saveBusinessVenue(BusinessVenue b) { return businessRepo.save(b); }
    @Override public Optional<BusinessVenue> findBusinessVenueById(Long id) { return businessRepo.findById(id); }
    @Override public void deleteBusinessVenue(Long id) { businessRepo.deleteById(id); }

    // ----- colleges -----
    @Override
    public Page<College> searchColleges(String q, Pageable pageable) {
        if (q == null || q.isBlank()) return collegeRepo.findAll(pageable);
        return collegeRepo.findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(q, q, pageable);
    }
    @Override public College saveCollege(College c) { return collegeRepo.save(c); }
    @Override public Optional<College> findCollegeById(Long id) { return collegeRepo.findById(id); }
    @Override public void deleteCollege(Long id) { collegeRepo.deleteById(id); }
}
