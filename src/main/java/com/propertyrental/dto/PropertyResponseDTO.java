package com.propertyrental.dto;

import com.propertyrental.entity.FurnishedStatus;
import com.propertyrental.entity.Property;
import com.propertyrental.entity.PropertyImage;
import com.propertyrental.entity.PropertyType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PropertyResponseDTO {
    private Long id;
    private String title;
    private String description;
    private PropertyType propertyType;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private Double rent;
    private Integer bedrooms;
    private Integer bathrooms;
    private Double area;
    private FurnishedStatus furnishedStatus;
    private String amenities;
    private boolean available;
    private Long ownerId;
    private String ownerName;
    private String ownerEmail;
    private String ownerPhone;
    private List<String> images = new ArrayList<>();
    private LocalDateTime createdAt;

    public PropertyResponseDTO() {}

    public PropertyResponseDTO(Property property) {
        this.id = property.getId();
        this.title = property.getTitle();
        this.description = property.getDescription();
        this.propertyType = property.getPropertyType();
        this.address = property.getAddress();
        this.city = property.getCity();
        this.state = property.getState();
        this.pincode = property.getPincode();
        this.rent = property.getRent();
        this.bedrooms = property.getBedrooms();
        this.bathrooms = property.getBathrooms();
        this.area = property.getArea();
        this.furnishedStatus = property.getFurnishedStatus();
        this.amenities = property.getAmenities();
        this.available = property.isAvailable();
        if (property.getOwner() != null) {
            this.ownerId = property.getOwner().getId();
            this.ownerName = property.getOwner().getName();
            this.ownerEmail = property.getOwner().getEmail();
            this.ownerPhone = property.getOwner().getPhone();
        }
        if (property.getImages() != null) {
            for (PropertyImage img : property.getImages()) {
                this.images.add(img.getImageUrl());
            }
        }
        this.createdAt = property.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Double getRent() {
        return rent;
    }

    public void setRent(Double rent) {
        this.rent = rent;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public FurnishedStatus getFurnishedStatus() {
        return furnishedStatus;
    }

    public void setFurnishedStatus(FurnishedStatus furnishedStatus) {
        this.furnishedStatus = furnishedStatus;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
