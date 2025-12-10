package com.smartcity.hyd.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

    @Autowired private JdbcTemplate jdbcTemplate; // for dynamic room tables

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
        return businessRepo.findByNameContainingIgnoreCaseOrBusinessTypeContainingIgnoreCaseOrLocationContainingIgnoreCase(
                q, q, q, pageable);
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

    // =============== HOTEL ROOMS (dynamic tables) ===============

    private String roomsTableName(String slug) {
        // extra safety: only allow [a-z0-9_]
        String safeSlug = slug.toLowerCase().replaceAll("[^a-z0-9_]", "_");
        return "rooms_" + safeSlug;
    }

    @Override
    public void createRoomsTableForHotel(String slug) {
        String table = roomsTableName(slug);
        String sql = "CREATE TABLE IF NOT EXISTS " + table + " ("
                + "id BIGINT PRIMARY KEY AUTO_INCREMENT, "
                + "room_no VARCHAR(50) UNIQUE NOT NULL, "
                + "capacity INT NOT NULL, "
                + "amenities VARCHAR(255), "
                + "price_per_day DECIMAL(10,2) NOT NULL, "
                + "status VARCHAR(20) NOT NULL"
                + ")";
        jdbcTemplate.execute(sql);
    }

    @Override
    public List<HotelRoom> getRoomsForHotel(String slug) {
        String table = roomsTableName(slug);
        String sql = "SELECT id, room_no, capacity, amenities, price_per_day, status FROM " + table + " ORDER BY id";
        return jdbcTemplate.query(sql, new RowMapper<HotelRoom>() {
            @Override
            public HotelRoom mapRow(ResultSet rs, int rowNum) throws SQLException {
                HotelRoom dto = new HotelRoom();
                dto.setId(rs.getLong("id"));
                dto.setRoomNo(rs.getInt("room_no"));
                dto.setCapacity(rs.getInt("capacity"));
                dto.setAmenities(rs.getString("amenities"));
                dto.setPricePerDay(rs.getDouble("price_per_day"));
                dto.setStatus(rs.getString("status"));
                return dto;
            }
        });
    }

    @Override
    public void addRoomToHotel(String slug, HotelRoom room) {
        String table = roomsTableName(slug);
        String sql = "INSERT INTO " + table
                + " (room_no, capacity, amenities, price_per_day, status) "
                + "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                room.getRoomNo(),
                room.getCapacity(),
                room.getAmenities(),
                room.getPricePerDay(),
                room.getStatus());
    }

    @Override
    public void updateRoomInHotel(String slug, HotelRoom room) {
        if (room.getId() == null) return;
        String table = roomsTableName(slug);
        String sql = "UPDATE " + table
                + " SET room_no = ?, capacity = ?, amenities = ?, price_per_day = ?, status = ? "
                + "WHERE id = ?";
        jdbcTemplate.update(sql,
                room.getRoomNo(),
                room.getCapacity(),
                room.getAmenities(),
                room.getPricePerDay(),
                room.getStatus(),
                room.getId());
    }

    @Override
    public void deleteRoomInHotel(String slug, Long roomId) {
        String table = roomsTableName(slug);
        String sql = "DELETE FROM " + table + " WHERE id = ?";
        jdbcTemplate.update(sql, roomId);
    }
}
