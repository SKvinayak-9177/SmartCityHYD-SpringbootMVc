package com.smartcity.hyd.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.smartcity.hyd.model.College;
import com.smartcity.hyd.repo.CollegeRepo;

@Service
public class CollegeService {

    @Autowired
    private CollegeRepo repo;

    public Page<College> search(String q, int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        if (q == null || q.isBlank()) return repo.findAll(pageable);

        return repo.findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(q, q, pageable);
    }

    public College saveNewCollege(String name, String location, double rating, MultipartFile image) throws Exception {

        if (repo.existsByName(name)) throw new Exception("College already exists!");

        College c = new College();
        c.setName(name);
        c.setLocation(location);
        c.setRating(rating);
        c.setSlug(generateSlug(name));

        if (!image.isEmpty()) c.setImage(image.getBytes());

        return repo.save(c);
    }

    public College updateCollege(Long id, String name, String location, double rating, MultipartFile image) throws Exception {
        College c = repo.findById(id).orElseThrow(() -> new Exception("College not found"));

        c.setName(name);
        c.setLocation(location);
        c.setRating(rating);
        c.setSlug(generateSlug(name));

        if (image != null && !image.isEmpty()) c.setImage(image.getBytes());

        return repo.save(c);
    }

    public void deleteCollege(Long id) {
        repo.deleteById(id);
    }

    private String generateSlug(String input) {
        return input.toLowerCase().replaceAll("[^a-z0-9]+", "-");
    }
}
