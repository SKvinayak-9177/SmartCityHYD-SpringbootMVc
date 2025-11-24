package com.smartcity.hyd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import com.smartcity.hyd.model.*;
import com.smartcity.hyd.service.*;
import com.smartcity.hyd.repo.*;

@Controller
public class UserController {

    @Autowired
    private UserServices service;

    @GetMapping("/user")
    public String showRform(Model model) {
        model.addAttribute("User", new UserModel());
        return "UserRegister";
    }

    @GetMapping("/userlogin")
    public String showLform(Model model) {
        model.addAttribute("User", new UserModel());
        return "UserLogin";
    }

    @PostMapping("/saveuser")
    public String saveUser(@ModelAttribute UserModel user, Model model) {
        service.save(user);
        model.addAttribute("user", user);
        return "Success";
    }

    @PostMapping("/loginuser")
    public String loginUser(@RequestParam String umail, @RequestParam String upassword, Model model) {

        UserModel user = service.validate(umail, upassword);

        if (user != null) {
            model.addAttribute("username", user.getUname());
            model.addAttribute("usermail", user.getUmail());
            return "HomePage";
        } else {
            return "Failure";
        }
    }

    @Autowired
    private HotelRepo hotelRepo;

    @Autowired
    private HotelService hotelService;

    @GetMapping("/view-hotels")
    public String viewHotels(@RequestParam(required=false) String q,
                             @RequestParam(defaultValue="0") int page,
                             @RequestParam(defaultValue="12") int size,
                             Model model) {

        var hotels = hotelService.search(q, page, size, Sort.by(Sort.Direction.DESC, "rating"));

        model.addAttribute("hotelsPage", hotels);
        model.addAttribute("q", q);
        return "view_hotels";
    }
    @Autowired
    private RestaurantRepo restauaratrepo;

    @Autowired
    private RestaurantService restaurantservice;
    @GetMapping("/view-restaurants")
    public String viewRestaurants(@RequestParam(required=false) String q,
                             @RequestParam(defaultValue="0") int page,
                             @RequestParam(defaultValue="12") int size,
                             Model model) {

        var restaurants = restaurantservice.search(q, page, size, Sort.by(Sort.Direction.DESC, "rating"));

        model.addAttribute("restaurantsPage", restaurants);
        model.addAttribute("q", q);
        return "view_restaurants";
    }
    @Autowired
    private TouristPlaceRepo touristrepo;

    @Autowired
    private TouristPlaceService touristservice;
    @GetMapping("/view-touristplaces")
    public String viewTouristPlaces(@RequestParam(required=false) String q,
                             @RequestParam(defaultValue="0") int page,
                             @RequestParam(defaultValue="12") int size,
                             Model model) {

        var touristplaces = touristservice.search(q, page, size, Sort.by(Sort.Direction.DESC, "rating"));

        model.addAttribute("touristplacesPage", touristplaces);
        model.addAttribute("q", q);
        return "view-touristplaces";
    }
    @Autowired
    private BusinessVenueRepo businessrepo;

    @Autowired
    private BusinessVenueService businessservice;
    @GetMapping("/view-businessvenues")
    public String viewBusinessVenues(@RequestParam(required=false) String q,
                             @RequestParam(defaultValue="0") int page,
                             @RequestParam(defaultValue="12") int size,
                             Model model) {

        var businessvenues = businessservice.search(q, page, size, Sort.by(Sort.Direction.DESC, "venueStartDate"));

        model.addAttribute("businessvenuesPage", businessvenues);
        model.addAttribute("q", q);
        return "view-businessvenues";
    }
    @Autowired
    private CollegeRepo collegerepo;

    @Autowired
    private CollegeService collegeservice;
    @GetMapping("/view-colleges")
    public String viewColleges(@RequestParam(required=false) String q,
                             @RequestParam(defaultValue="0") int page,
                             @RequestParam(defaultValue="12") int size,
                             Model model) {

        var colleges = collegeservice.search(q, page, size, Sort.by(Sort.Direction.DESC, "rating"));

        model.addAttribute("collegesPage", colleges);
        model.addAttribute("q", q);
        return "view-colleges";
    }
    
    @Autowired
    private PgRepo pgrepo;

    @Autowired
    private PgService pgservice;
    @GetMapping("/view-pgs")
    public String viewPgs(@RequestParam(required=false) String q,
                             @RequestParam(defaultValue="0") int page,
                             @RequestParam(defaultValue="12") int size,
                             Model model) {

        var pgs = pgservice.search(q, page, size, Sort.by(Sort.Direction.DESC, "rating"));

        model.addAttribute("pgsPage", pgs);
        model.addAttribute("q", q);
        return "view-pgs";
    }
    
}
