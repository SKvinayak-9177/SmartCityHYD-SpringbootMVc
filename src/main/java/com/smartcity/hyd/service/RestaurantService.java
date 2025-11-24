package com.smartcity.hyd.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.smartcity.hyd.model.Restaurant;
import com.smartcity.hyd.repo.RestaurantRepo;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepo repo;

    public Page<Restaurant> search(String q, int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        if (q == null || q.isBlank()) return repo.findAll(pageable);

        return repo.findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(q, q, pageable);
    }

    public Restaurant saveNewRestaurant(String name, String location, double rating, String timings, MultipartFile image) throws Exception {

        if (repo.existsByName(name))
            throw new Exception("Restaurant already exists!");

        Restaurant r = new Restaurant();
        r.setName(name);
        r.setLocation(location);
        r.setRating(rating);
        r.setTimings(timings);
        r.setSlug(generateSlug(name));

        if (!image.isEmpty()) r.setImage(image.getBytes());

        return repo.save(r);
    }

    public Restaurant updateRestaurant(Long id, String name, String location, double rating, String timings, MultipartFile image) throws Exception {
        Restaurant r = repo.findById(id).orElseThrow(() -> new Exception("Restaurant not found"));

        r.setName(name);
        r.setLocation(location);
        r.setRating(rating);
        r.setTimings(timings);
        r.setSlug(generateSlug(name));

        if (image != null && !image.isEmpty()) r.setImage(image.getBytes());

        return repo.save(r);
    }

    public void deleteRestaurant(Long id) {
        repo.deleteById(id);
    }

    private String generateSlug(String input) {
        return input.toLowerCase().replaceAll("[^a-z0-9]+", "-");
    }
}
