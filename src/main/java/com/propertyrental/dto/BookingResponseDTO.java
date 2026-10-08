package com.propertyrental.dto;

import com.propertyrental.entity.Booking;
import com.propertyrental.entity.BookingStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingResponseDTO {
    private Long id;
    private Long propertyId;
    private String propertyTitle;
    private String propertyCity;
    private String propertyAddress;
    private String propertyImage;
    private Double propertyRent;

    private Long tenantId;
    private String tenantName;
    private String tenantEmail;
    private String tenantPhone;

    private Long ownerId;
    private String ownerName;
    private String ownerEmail;
    private String ownerPhone;

    private LocalDate startDate;
    private LocalDate endDate;
    private Double monthlyRent;
    private Double totalAmount;
    private BookingStatus status;
    private String notes;
    private LocalDateTime createdAt;

    private boolean paid;
    private String transactionId;

    public BookingResponseDTO() {}

    public BookingResponseDTO(Booking booking) {
        this.id = booking.getId();
        if (booking.getProperty() != null) {
            this.propertyId = booking.getProperty().getId();
            this.propertyTitle = booking.getProperty().getTitle();
            this.propertyCity = booking.getProperty().getCity();
            this.propertyAddress = booking.getProperty().getAddress();
            this.propertyRent = booking.getProperty().getRent();
            if (booking.getProperty().getImages() != null && !booking.getProperty().getImages().isEmpty()) {
                this.propertyImage = booking.getProperty().getImages().get(0).getImageUrl();
            }
        }
        if (booking.getTenant() != null) {
            this.tenantId = booking.getTenant().getId();
            this.tenantName = booking.getTenant().getName();
            this.tenantEmail = booking.getTenant().getEmail();
            this.tenantPhone = booking.getTenant().getPhone();
        }
        if (booking.getOwner() != null) {
            this.ownerId = booking.getOwner().getId();
            this.ownerName = booking.getOwner().getName();
            this.ownerEmail = booking.getOwner().getEmail();
            this.ownerPhone = booking.getOwner().getPhone();
        }
        this.startDate = booking.getStartDate();
        this.endDate = booking.getEndDate();
        this.monthlyRent = booking.getMonthlyRent();
        this.totalAmount = booking.getTotalAmount();
        this.status = booking.getStatus();
        this.notes = booking.getNotes();
        this.createdAt = booking.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyTitle() {
        return propertyTitle;
    }

    public void setPropertyTitle(String propertyTitle) {
        this.propertyTitle = propertyTitle;
    }

    public String getPropertyCity() {
        return propertyCity;
    }

    public void setPropertyCity(String propertyCity) {
        this.propertyCity = propertyCity;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getPropertyImage() {
        return propertyImage;
    }

    public void setPropertyImage(String propertyImage) {
        this.propertyImage = propertyImage;
    }

    public Double getPropertyRent() {
        return propertyRent;
    }

    public void setPropertyRent(Double propertyRent) {
        this.propertyRent = propertyRent;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantEmail() {
        return tenantEmail;
    }

    public void setTenantEmail(String tenantEmail) {
        this.tenantEmail = tenantEmail;
    }

    public String getTenantPhone() {
        return tenantPhone;
    }

    public void setTenantPhone(String tenantPhone) {
        this.tenantPhone = tenantPhone;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getMonthlyRent() {
        return monthlyRent;
    }

    public void setMonthlyRent(Double monthlyRent) {
        this.monthlyRent = monthlyRent;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
