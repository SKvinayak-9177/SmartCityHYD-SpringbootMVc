package com.smartcity.hyd.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.smartcity.hyd.model.BusinessVenue;
import com.smartcity.hyd.repo.BusinessVenueRepo;

@Service
public class BusinessVenueService {

    @Autowired
    private BusinessVenueRepo repo;

    public Page<BusinessVenue> search(String q, int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);

        if (q == null || q.isBlank()) return repo.findAll(pageable);

        return repo.findByNameContainingIgnoreCaseOrBusinessTypeContainingIgnoreCaseOrLocationContainingIgnoreCase(
                q, q, q, pageable
        );
    }

    public BusinessVenue saveNewVenue(String name, String type, String location, LocalDate startDate, MultipartFile image) throws Exception {

        if (repo.existsByName(name)) throw new Exception("Business venue already exists!");

        BusinessVenue b = new BusinessVenue();
        b.setName(name);
        b.setBusinessType(type);
        b.setLocation(location);
        b.setVenueStartDate(startDate);
        b.setSlug(generateSlug(name));

        if (!image.isEmpty()) b.setImage(image.getBytes());

        return repo.save(b);
    }

    public BusinessVenue updateVenue(Long id, String name, String type, String location, LocalDate startDate, MultipartFile image) throws Exception {
        BusinessVenue b = repo.findById(id)
                .orElseThrow(() -> new Exception("Business venue not found"));

        b.setName(name);
        b.setBusinessType(type);
        b.setLocation(location);
        b.setVenueStartDate(startDate);
        b.setSlug(generateSlug(name));

        if (image != null && !image.isEmpty()) b.setImage(image.getBytes());

        return repo.save(b);
    }

    public void deleteVenue(Long id) {
        repo.deleteById(id);
    }

    private String generateSlug(String input) {
        return input.toLowerCase().replaceAll("[^a-z0-9]+", "-");
    }
}
