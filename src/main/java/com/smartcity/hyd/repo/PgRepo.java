package com.smartcity.hyd.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smartcity.hyd.model.PgAccommodation;

public interface PgRepo extends JpaRepository<PgAccommodation, Long> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
    Page<PgAccommodation> findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(String name, String location, Pageable pageable);
}
