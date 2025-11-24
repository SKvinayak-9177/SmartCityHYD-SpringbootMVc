package com.smartcity.hyd.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smartcity.hyd.model.BusinessVenue;

public interface BusinessVenueRepo extends JpaRepository<BusinessVenue, Long> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);

    // search by name OR businessType OR location
    Page<BusinessVenue> findByNameContainingIgnoreCaseOrBusinessTypeContainingIgnoreCaseOrLocationContainingIgnoreCase(
            String name,
            String businessType,
            String location,
            Pageable pageable
    );
}
