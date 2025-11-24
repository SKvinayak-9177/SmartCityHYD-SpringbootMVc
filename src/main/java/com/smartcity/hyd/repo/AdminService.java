package com.smartcity.hyd.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.smartcity.hyd.model.*;

public interface AdminService {
    long countHotels();
    long countRestaurants();
    long countPgs();
    long countTouristPlaces();
    long countBusinessVenues();
    long countColleges();
    
    Page<Hotel> searchHotels(String q, Pageable pageable);
    Hotel saveHotel(Hotel hotel);
    Optional<Hotel> findHotelById(Long id);
    void deleteHotel(Long id);
    
    Page<College> searchColleges(String q, Pageable pageable);
    College saveCollege(College college);
    Optional<College> findCollegeById(Long id);
    void deleteCollege(Long id);
    
    Page<Restaurant> searchRestaurants(String q, Pageable pageable);
    Restaurant saveRestaurant(Restaurant restaurant);
    Optional<Restaurant> findRestaurantById(Long id);
    void deleteRestaurant(Long id);
    
    Page<PgAccommodation> searchPgAccommodations(String q, Pageable pageable);
    PgAccommodation savePgAccommodation(PgAccommodation pgaccommodation);
    Optional<PgAccommodation> findPgAccommodationById(Long id);
    void deletePgAccommodation(Long id);
    
    Page<TouristPlace> searchTouristPlaces(String q, Pageable pageable);
    TouristPlace saveTouristPlace(TouristPlace touristplace);
    Optional<TouristPlace> findTouristPlaceById(Long id);
    void deleteTouristPlace(Long id);
    
    Page<BusinessVenue> searchBusinessVenues(String q, Pageable pageable);
    BusinessVenue saveBusinessVenue(BusinessVenue businessvenue);
    Optional<BusinessVenue> findBusinessVenueById(Long id);
    void deleteBusinessVenue(Long id);
}

