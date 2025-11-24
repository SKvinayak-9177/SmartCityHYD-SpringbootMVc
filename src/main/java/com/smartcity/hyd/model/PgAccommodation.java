package com.smartcity.hyd.model;

import java.time.LocalDateTime;
import java.util.Arrays;

import jakarta.persistence.*;

@Entity
@Table(name = "pgs")
public class PgAccommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String name;

    private String location;
    private double rating;

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
	public PgAccommodation() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PgAccommodation(Long id, String name, String location, double rating, byte[] image, String slug,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.name = name;
		this.location = location;
		this.rating = rating;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
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
		return "PgAccommodation [id=" + id + ", name=" + name + ", location=" + location + ", rating=" + rating
				+ ", image=" + Arrays.toString(image) + ", slug=" + slug + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}
    
}

