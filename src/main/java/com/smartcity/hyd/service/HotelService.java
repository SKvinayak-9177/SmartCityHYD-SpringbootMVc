package com.smartcity.hyd.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.smartcity.hyd.model.Hotel;
import com.smartcity.hyd.repo.HotelRepo;

@Service
public class HotelService {

    @Autowired
    private HotelRepo repo;

    public Page<Hotel> search(String q, int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        if (q == null || q.isBlank()) return repo.findAll(pageable);

        return repo.findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(q, q, pageable);
    }

    public Hotel saveNewHotel(String name, String location, double rating, MultipartFile image) throws Exception {
        if (repo.existsByName(name))
            throw new Exception("Hotel name already exists!");

        Hotel h = new Hotel();
        h.setName(name);
        h.setLocation(location);
        h.setRating(rating);
        h.setSlug(generateSlug(name));

        if (!image.isEmpty()) h.setImage(image.getBytes());

        return repo.save(h);
    }

    public Hotel updateHotel(Long id, String name, String location, double rating, MultipartFile image) throws Exception {
        Hotel h = repo.findById(id).orElseThrow(() -> new Exception("Hotel not found"));

        h.setName(name);
        h.setLocation(location);
        h.setRating(rating);
        h.setSlug(generateSlug(name));

        if (image != null && !image.isEmpty()) {
            h.setImage(image.getBytes());
        }

        return repo.save(h);
    }

    public void deleteHotel(Long id) {
        repo.deleteById(id);
    }

    private String generateSlug(String input) {
        return input.toLowerCase().replaceAll("[^a-z0-9]+", "-");
    }
}
