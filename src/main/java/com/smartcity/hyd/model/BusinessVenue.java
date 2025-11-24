package com.smartcity.hyd.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import jakarta.persistence.*;

@Entity
@Table(name = "business_venues")
public class BusinessVenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String name;

    private String businessType;
    private String location;
    private LocalDate venueStartDate;

    @Lob
    @Column(columnDefinition="LONGBLOB")
    private byte[] image;

    @Column(nullable=false, unique=true)
    private String slug;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
	public BusinessVenue() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BusinessVenue(Long id, String name, String businessType, String location, LocalDate venueStartDate,
			byte[] image, String slug, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.name = name;
		this.businessType = businessType;
		this.location = location;
		this.venueStartDate = venueStartDate;
		this.image = image;
		this.slug = slug;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public LocalDate getVenueStartDate() {
		return venueStartDate;
	}
	public void setVenueStartDate(LocalDate venueStartDate) {
		this.venueStartDate = venueStartDate;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	@Override
	public String toString() {
		return "BusinessVenue [id=" + id + ", name=" + name + ", businessType=" + businessType + ", location="
				+ location + ", venueStartDate=" + venueStartDate + ", image=" + Arrays.toString(image) + ", slug="
				+ slug + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
    
}

