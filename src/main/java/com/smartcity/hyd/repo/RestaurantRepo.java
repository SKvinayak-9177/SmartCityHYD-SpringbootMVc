package com.smartcity.hyd.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smartcity.hyd.model.Restaurant;

public interface RestaurantRepo extends JpaRepository<Restaurant, Long> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
    Page<Restaurant> findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(String name, String location, Pageable pageable);
}
