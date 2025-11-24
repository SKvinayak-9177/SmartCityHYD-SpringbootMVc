package com.smartcity.hyd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.smartcity.hyd.repo.*;

@RestController
public class ImageController {

    @Autowired private HotelRepo hotelRepo;
    @Autowired private RestaurantRepo restaurantRepo;
    @Autowired private PgRepo pgRepo;
    @Autowired private TouristPlaceRepo touristRepo;
    @Autowired private BusinessVenueRepo businessRepo;
    @Autowired private CollegeRepo collegeRepo;

    // -------------------- HOTEL --------------------
    @GetMapping("/hotel/image/{id}")
    public ResponseEntity<byte[]> hotelImage(@PathVariable Long id) {
        return imageResponse(hotelRepo.findById(id).map(h -> h.getImage()).orElse(null));
    }

    // -------------------- RESTAURANT --------------------
    @GetMapping("/restaurant/image/{id}")
    public ResponseEntity<byte[]> restaurantImage(@PathVariable Long id) {
        return imageResponse(restaurantRepo.findById(id).map(r -> r.getImage()).orElse(null));
    }

    // -------------------- PG --------------------
    @GetMapping("/pg/image/{id}")
    public ResponseEntity<byte[]> pgImage(@PathVariable Long id) {
        return imageResponse(pgRepo.findById(id).map(p -> p.getImage()).orElse(null));
    }

    // -------------------- TOURIST PLACE --------------------
    @GetMapping("/tourist/image/{id}")
    public ResponseEntity<byte[]> touristImage(@PathVariable Long id) {
        return imageResponse(touristRepo.findById(id).map(t -> t.getImage()).orElse(null));
    }

    // -------------------- BUSINESS VENUE --------------------
    @GetMapping("/business/image/{id}")
    public ResponseEntity<byte[]> businessImage(@PathVariable Long id) {
        return imageResponse(businessRepo.findById(id).map(b -> b.getImage()).orElse(null));
    }

    // -------------------- COLLEGE --------------------
    @GetMapping("/college/image/{id}")
    public ResponseEntity<byte[]> collegeImage(@PathVariable Long id) {
        return imageResponse(collegeRepo.findById(id).map(c -> c.getImage()).orElse(null));
    }

    // -------------------- COMMON RESPONSE METHOD --------------------
    private ResponseEntity<byte[]> imageResponse(byte[] bytes) {
        if (bytes == null || bytes.length == 0) 
            return ResponseEntity.notFound().build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setCacheControl(CacheControl.noCache());

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}
