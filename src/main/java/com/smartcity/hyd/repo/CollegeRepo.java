package com.smartcity.hyd.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smartcity.hyd.model.College;

public interface CollegeRepo extends JpaRepository<College, Long> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
    Page<College> findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(String name, String location, Pageable pageable);
}
