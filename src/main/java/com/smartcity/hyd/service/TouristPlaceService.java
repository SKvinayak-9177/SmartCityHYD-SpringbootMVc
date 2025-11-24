package com.smartcity.hyd.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.smartcity.hyd.model.TouristPlace;
import com.smartcity.hyd.repo.TouristPlaceRepo;

@Service
public class TouristPlaceService {

    @Autowired
    private TouristPlaceRepo repo;

    public Page<TouristPlace> search(String q, int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        if (q == null || q.isBlank()) return repo.findAll(pageable);

        return repo.findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(q, q, pageable);
    }

    public TouristPlace saveNewTouristPlace(String name, String location, double rating, MultipartFile image) throws Exception {

        if (repo.existsByName(name))
            throw new Exception("Tourist place already exists!");

        TouristPlace t = new TouristPlace();
        t.setName(name);
        t.setLocation(location);
        t.setRating(rating);
        t.setSlug(generateSlug(name));

        if (!image.isEmpty()) t.setImage(image.getBytes());

        return repo.save(t);
    }

    public TouristPlace updateTouristPlace(Long id, String name, String location, double rating, MultipartFile image) throws Exception {
        TouristPlace t = repo.findById(id).orElseThrow(() -> new Exception("Tourist place not found"));

        t.setName(name);
        t.setLocation(location);
        t.setRating(rating);
        t.setSlug(generateSlug(name));

        if (image != null && !image.isEmpty()) t.setImage(image.getBytes());

        return repo.save(t);
    }

    public void deleteTouristPlace(Long id) {
        repo.deleteById(id);
    }

    private String generateSlug(String input) {
        return input.toLowerCase().replaceAll("[^a-z0-9]+", "-");
    }
}
