package com.smartcity.hyd.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.smartcity.hyd.model.PgAccommodation;
import com.smartcity.hyd.repo.PgRepo;

@Service
public class PgService {

    @Autowired
    private PgRepo repo;

    public Page<PgAccommodation> search(String q, int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        if (q == null || q.isBlank()) return repo.findAll(pageable);

        return repo.findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(q, q, pageable);
    }

    public PgAccommodation saveNewPg(String name, String location, double rating, MultipartFile image) throws Exception {

        if (repo.existsByName(name)) throw new Exception("PG already exists!");

        PgAccommodation p = new PgAccommodation();
        p.setName(name);
        p.setLocation(location);
        p.setRating(rating);
        p.setSlug(generateSlug(name));

        if (!image.isEmpty()) p.setImage(image.getBytes());

        return repo.save(p);
    }

    public PgAccommodation updatePg(Long id, String name, String location, double rating, MultipartFile image) throws Exception {
        PgAccommodation p = repo.findById(id).orElseThrow(() -> new Exception("PG not found"));

        p.setName(name);
        p.setLocation(location);
        p.setRating(rating);
        p.setSlug(generateSlug(name));

        if (image != null && !image.isEmpty()) p.setImage(image.getBytes());

        return repo.save(p);
    }

    public void deletePg(Long id) {
        repo.deleteById(id);
    }

    private String generateSlug(String input) {
        return input.toLowerCase().replaceAll("[^a-z0-9]+", "-");
    }
}
