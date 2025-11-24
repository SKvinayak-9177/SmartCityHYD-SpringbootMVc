package com.smartcity.hyd.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smartcity.hyd.model.Hotel;

public interface HotelRepo extends JpaRepository<Hotel, Long> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
    Page<Hotel> findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(String name, String location, Pageable pageable);
}
