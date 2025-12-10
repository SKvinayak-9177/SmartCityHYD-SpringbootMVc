package com.smartcity.hyd.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.smartcity.hyd.model.*;
import com.smartcity.hyd.repo.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private AdminService adminService;

    // Dashboard
    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("hotelCount", adminService.countHotels());
        model.addAttribute("restaurantCount", adminService.countRestaurants());
        model.addAttribute("pgCount", adminService.countPgs());
        model.addAttribute("touristCount", adminService.countTouristPlaces());
        model.addAttribute("businessCount", adminService.countBusinessVenues());
        model.addAttribute("collegeCount", adminService.countColleges());
        return "admin_dashboard";
    }

    // ==================== HOTELS ====================

    @GetMapping("/hotels")
    public String hotelsPage(@RequestParam(required=false) String q,
                             @RequestParam(defaultValue="0") int page,
                             @RequestParam(defaultValue="12") int size,
                             Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Hotel> hotels = adminService.searchHotels(q, pageable);
        model.addAttribute("hotelsPage", hotels);
        model.addAttribute("q", q);
        return "admin_hotels";
    }

    @PostMapping("/hotels/add")
    public String addHotel(@RequestParam String name,
                           @RequestParam String location,
                           @RequestParam double rating,
                           @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        Hotel h = new Hotel();
        h.setName(name);
        h.setLocation(location);
        h.setRating(rating);

        if (!imageFile.isEmpty()) {
            h.setImage(imageFile.getBytes());
        }

        // Step 1: initial slug without ID (required because slug cannot be null)
        String tempSlug = generateSlug(name);
        h.setSlug(tempSlug);

        // Step 2: first save (OK because slug is not null)
        Hotel saved = adminService.saveHotel(h);

        // Step 3: now update slug with unique ID (recommended)
        String finalSlug = tempSlug + "-" + saved.getId();
        saved.setSlug(finalSlug);

        // Step 4: save again
        adminService.saveHotel(saved);

        // Step 5: create dynamic room table
        adminService.createRoomsTableForHotel(finalSlug);

        return "redirect:/admin/hotels";
    }


    @GetMapping("/hotels/edit/{id}")
    public String editHotelForm(@PathVariable Long id, Model model) {
        model.addAttribute("hotel", adminService.findHotelById(id).orElse(null));
        return "edit_hotel_form";
    }

    @PostMapping("/hotels/update/{id}")
    public String updateHotel(@PathVariable Long id,
                              @RequestParam String name,
                              @RequestParam String location,
                              @RequestParam double rating,
                              @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {

        Hotel h = adminService.findHotelById(id).orElseThrow();
        h.setName(name);
        h.setLocation(location);
        h.setRating(rating);
        if (imageFile != null && !imageFile.isEmpty()) h.setImage(imageFile.getBytes());
        // DO NOT change slug here – it must stay the same for the room table.

        adminService.saveHotel(h);
        return "redirect:/admin/hotels";
    }

    @PostMapping("/hotels/delete")
    public String deleteHotel(@RequestParam Long id) {
        // note: this does NOT drop the dynamic room table – you can add that if you want.
        adminService.deleteHotel(id);
        return "redirect:/admin/hotels";
    }

    // ----- Hotel details + rooms page -----

    @GetMapping("/hotels/{id}")
    public String hotelDetails(@PathVariable Long id, Model model) {
        Hotel hotel = adminService.findHotelById(id).orElseThrow();
        String slug = hotel.getSlug();

        // safety: ensure table exists
        adminService.createRoomsTableForHotel(slug);

        java.util.List<HotelRoom> rooms = adminService.getRoomsForHotel(slug);

        model.addAttribute("hotel", hotel);
        model.addAttribute("rooms", rooms);
        model.addAttribute("roomForm", new HotelRoom());
        return "hotel_details";
    }

    @PostMapping("/hotels/{id}/rooms/add")
    public String addRoom(@PathVariable Long id,
                          @ModelAttribute("roomForm") HotelRoom room) {

        Hotel hotel = adminService.findHotelById(id).orElseThrow();
        adminService.addRoomToHotel(hotel.getSlug(), room);
        return "redirect:/admin/hotels/" + id;
    }

    @PostMapping("/hotels/{id}/rooms/update")
    public String updateRoom(@PathVariable Long id,
                             @ModelAttribute HotelRoom room) {

        Hotel hotel = adminService.findHotelById(id).orElseThrow();
        adminService.updateRoomInHotel(hotel.getSlug(), room);
        return "redirect:/admin/hotels/" + id;
    }

    @PostMapping("/hotels/{id}/rooms/delete")
    public String deleteRoom(@PathVariable Long id,
                             @RequestParam Long roomId) {

        Hotel hotel = adminService.findHotelById(id).orElseThrow();
        adminService.deleteRoomInHotel(hotel.getSlug(), roomId);
        return "redirect:/admin/hotels/" + id;
    }

    // ==================== RESTAURANTS ====================

    @GetMapping("/restaurants")
    public String restaurantsPage(@RequestParam(required=false) String q,
                                  @RequestParam(defaultValue="0") int page,
                                  @RequestParam(defaultValue="12") int size,
                                  Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Restaurant> restaurants = adminService.searchRestaurants(q, pageable);
        model.addAttribute("restaurantsPage", restaurants);
        model.addAttribute("q", q);
        return "admin_restaurants";
    }

    @PostMapping("/restaurants/add")
    public String addRestaurant(@RequestParam String name,
                                @RequestParam String location,
                                @RequestParam double rating,
                                @RequestParam String timings,
                                @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
        Restaurant r = new Restaurant();
        r.setName(name); r.setLocation(location); r.setRating(rating); r.setTimings(timings);
        if (imageFile != null && !imageFile.isEmpty()) r.setImage(imageFile.getBytes());
        r.setSlug(generateSlug(name));
        adminService.saveRestaurant(r);
        return "redirect:/admin/restaurants";
    }

    @GetMapping("/restaurants/edit/{id}")
    public String editRestaurantForm(@PathVariable Long id, Model model) {
        model.addAttribute("restaurant", adminService.findRestaurantById(id).orElse(null));
        return "edit_restaurant_form";
    }

    @PostMapping("/restaurants/update/{id}")
    public String updateRestaurant(@PathVariable Long id,
                                   @RequestParam String name,
                                   @RequestParam String location,
                                   @RequestParam double rating,
                                   @RequestParam String timings,
                                   @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
        Restaurant r = adminService.findRestaurantById(id).orElseThrow();
        r.setName(name); r.setLocation(location); r.setRating(rating); r.setTimings(timings);
        if (imageFile != null && !imageFile.isEmpty()) r.setImage(imageFile.getBytes());
        r.setSlug(generateSlug(name));
        adminService.saveRestaurant(r);
        return "redirect:/admin/restaurants";
    }

    @PostMapping("/restaurants/delete")
    public String deleteRestaurant(@RequestParam Long id) {
        adminService.deleteRestaurant(id);
        return "redirect:/admin/restaurants";
    }

    // ==================== PGs ====================

    @GetMapping("/pgs")
    public String pgsPage(@RequestParam(required=false) String q,
                          @RequestParam(defaultValue="0") int page,
                          @RequestParam(defaultValue="12") int size,
                          Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PgAccommodation> pgs = adminService.searchPgAccommodations(q, pageable);
        model.addAttribute("pgsPage", pgs);
        model.addAttribute("q", q);
        return "admin_pgs";
    }

    @PostMapping("/pgs/add")
    public String addPg(@RequestParam String name,
                        @RequestParam String location,
                        @RequestParam double rating,
                        @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
        PgAccommodation p = new PgAccommodation();
        p.setName(name); p.setLocation(location); p.setRating(rating);
        if (imageFile != null && !imageFile.isEmpty()) p.setImage(imageFile.getBytes());
        p.setSlug(generateSlug(name));
        adminService.savePgAccommodation(p);
        return "redirect:/admin/pgs";
    }

    @GetMapping("/pgs/edit/{id}")
    public String editPgForm(@PathVariable Long id, Model model) {
        model.addAttribute("pg", adminService.findPgAccommodationById(id).orElse(null));
        return "edit_pg_form";
    }

    @PostMapping("/pgs/update/{id}")
    public String updatePg(@PathVariable Long id,
                           @RequestParam String name,
                           @RequestParam String location,
                           @RequestParam double rating,
                           @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
        PgAccommodation p = adminService.findPgAccommodationById(id).orElseThrow();
        p.setName(name); p.setLocation(location); p.setRating(rating);
        if (imageFile != null && !imageFile.isEmpty()) p.setImage(imageFile.getBytes());
        p.setSlug(generateSlug(name));
        adminService.savePgAccommodation(p);
        return "redirect:/admin/pgs";
    }

    @PostMapping("/pgs/delete")
    public String deletePg(@RequestParam Long id) {
        adminService.deletePgAccommodation(id);
        return "redirect:/admin/pgs";
    }

    // ==================== TOURIST PLACES ====================

    @GetMapping("/tourist-places")
    public String touristPage(@RequestParam(required=false) String q,
                              @RequestParam(defaultValue="0") int page,
                              @RequestParam(defaultValue="12") int size,
                              Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<TouristPlace> places = adminService.searchTouristPlaces(q, pageable);
        model.addAttribute("tpPage", places);
        model.addAttribute("q", q);
        return "admin_touristPlaces";
    }

    @PostMapping("/tourist/add")
    public String addTourist(@RequestParam String name,
                             @RequestParam String location,
                             @RequestParam double rating,
                             @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
        TouristPlace t = new TouristPlace();
        t.setName(name); t.setLocation(location); t.setRating(rating);
        if (imageFile != null && !imageFile.isEmpty()) t.setImage(imageFile.getBytes());
        t.setSlug(generateSlug(name));
        adminService.saveTouristPlace(t);
        return "redirect:/admin/tourist-places";
    }

    @GetMapping("/tourist/edit/{id}")
    public String editTouristForm(@PathVariable Long id, Model model) {
        model.addAttribute("place", adminService.findTouristPlaceById(id).orElse(null));
        return "edit_tourist_form";
    }

    @PostMapping("/tourist/update/{id}")
    public String updateTourist(@PathVariable Long id,
                                @RequestParam String name,
                                @RequestParam String location,
                                @RequestParam double rating,
                                @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
        TouristPlace t = adminService.findTouristPlaceById(id).orElseThrow();
        t.setName(name); t.setLocation(location); t.setRating(rating);
        if (imageFile != null && !imageFile.isEmpty()) t.setImage(imageFile.getBytes());
        t.setSlug(generateSlug(name));
        adminService.saveTouristPlace(t);
        return "redirect:/admin/tourist-places";
    }

    @PostMapping("/tourist/delete")
    public String deleteTourist(@RequestParam Long id) {
        adminService.deleteTouristPlace(id);
        return "redirect:/admin/tourist-places";
    }

    // ==================== BUSINESS VENUES ====================

    @GetMapping("/business-venues")
    public String businessPage(@RequestParam(required=false) String q,
                               @RequestParam(defaultValue="0") int page,
                               @RequestParam(defaultValue="12") int size,
                               Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BusinessVenue> bv = adminService.searchBusinessVenues(q, pageable);
        model.addAttribute("venuePage", bv);
        model.addAttribute("q", q);
        return "admin_businessvenues";
    }

    @PostMapping("/business/add")
    public String addBusiness(@RequestParam String name,
                              @RequestParam String businessType,
                              @RequestParam String location,
                              @RequestParam(required=false) String venueStartDate,
                              @RequestParam(value="imageFile", required=false) MultipartFile imageFile,
                              Model model) throws IOException {
        BusinessVenue b = new BusinessVenue();
        b.setName(name); b.setBusinessType(businessType); b.setLocation(location);
        try {
            if (venueStartDate != null && !venueStartDate.isBlank()) {
                b.setVenueStartDate(LocalDate.parse(venueStartDate));
            }
        } catch (DateTimeParseException ex) {
            model.addAttribute("errorMessage", "Invalid date format. Use yyyy-MM-dd");
            return "admin_businessvenues";
        }
        if (imageFile != null && !imageFile.isEmpty()) b.setImage(imageFile.getBytes());
        b.setSlug(generateSlug(name));
        adminService.saveBusinessVenue(b);
        return "redirect:/admin/business-venues";
    }

    @GetMapping("/business/edit/{id}")
    public String editBusinessForm(@PathVariable Long id, Model model) {
        model.addAttribute("business", adminService.findBusinessVenueById(id).orElse(null));
        return "edit_business_form";
    }

    @PostMapping("/business/update/{id}")
    public String updateBusiness(@PathVariable Long id,
                                 @RequestParam String name,
                                 @RequestParam String businessType,
                                 @RequestParam String location,
                                 @RequestParam(required=false) String venueStartDate,
                                 @RequestParam(value="imageFile", required=false) MultipartFile imageFile,
                                 Model model) throws IOException {
        BusinessVenue b = adminService.findBusinessVenueById(id).orElseThrow();
        b.setName(name); b.setBusinessType(businessType); b.setLocation(location);
        try {
            if (venueStartDate != null && !venueStartDate.isBlank()) {
                b.setVenueStartDate(LocalDate.parse(venueStartDate));
            } else {
                b.setVenueStartDate(null);
            }
        } catch (DateTimeParseException ex) {
            model.addAttribute("errorMessage", "Invalid date format. Use yyyy-MM-dd");
            return "edit_business_form";
        }
        if (imageFile != null && !imageFile.isEmpty()) b.setImage(imageFile.getBytes());
        b.setSlug(generateSlug(name));
        adminService.saveBusinessVenue(b);
        return "redirect:/admin/business-venues";
    }

    @PostMapping("/business/delete")
    public String deleteBusiness(@RequestParam Long id) {
        adminService.deleteBusinessVenue(id);
        return "redirect:/admin/business-venues";
    }

    // ==================== COLLEGES ====================

    @GetMapping("/colleges")
    public String collegesPage(@RequestParam(required=false) String q,
                               @RequestParam(defaultValue="0") int page,
                               @RequestParam(defaultValue="12") int size,
                               Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<College> colleges = adminService.searchColleges(q, pageable);
        model.addAttribute("collegesPage", colleges);
        model.addAttribute("q", q);
        return "admin_colleges";
    }

    @PostMapping("/colleges/add")
    public String addCollege(@RequestParam String name,
                             @RequestParam String location,
                             @RequestParam double rating,
                             @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
        College c = new College();
        c.setName(name); c.setLocation(location); c.setRating(rating);
        if (imageFile != null && !imageFile.isEmpty()) c.setImage(imageFile.getBytes());
        c.setSlug(generateSlug(name));
        adminService.saveCollege(c);
        return "redirect:/admin/colleges";
    }

    @GetMapping("/colleges/edit/{id}")
    public String editCollegeForm(@PathVariable Long id, Model model) {
        model.addAttribute("college", adminService.findCollegeById(id).orElse(null));
        return "edit_college_form";
    }

    @PostMapping("/colleges/update/{id}")
    public String updateCollege(@PathVariable Long id,
                                @RequestParam String name,
                                @RequestParam String location,
                                @RequestParam double rating,
                                @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
        College c = adminService.findCollegeById(id).orElseThrow();
        c.setName(name); c.setLocation(location); c.setRating(rating);
        if (imageFile != null && !imageFile.isEmpty()) c.setImage(imageFile.getBytes());
        c.setSlug(generateSlug(name));
        adminService.saveCollege(c);
        return "redirect:/admin/colleges";
    }

    @PostMapping("/colleges/delete")
    public String deleteCollege(@RequestParam Long id) {
        adminService.deleteCollege(id);
        return "redirect:/admin/colleges";
    }

    // helper
    private String generateSlug(String name) {
        if (name == null) return null;
        return name.trim()
                   .toLowerCase()
                   .replaceAll("[^a-z0-9\\s-]", "")
                   .replaceAll("\\s+","-");
    }
}
