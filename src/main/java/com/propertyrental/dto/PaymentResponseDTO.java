package com.propertyrental.dto;

import com.propertyrental.entity.Payment;
import com.propertyrental.entity.PaymentStatus;
import java.time.LocalDateTime;

public class PaymentResponseDTO {
    private Long id;
    private Long bookingId;
    private Long propertyId;
    private String propertyTitle;
    private String propertyAddress;
    private String propertyCity;

    private Long tenantId;
    private String tenantName;
    private String tenantEmail;
    private String tenantPhone;

    private Long ownerId;
    private String ownerName;
    private String ownerEmail;

    private Double amount;
    private LocalDateTime paymentDate;
    private PaymentStatus paymentStatus;
    private String transactionId;
    private String paymentMethod;

    public PaymentResponseDTO() {}

    public PaymentResponseDTO(Payment payment) {
        this.id = payment.getId();
        if (payment.getBooking() != null) {
            this.bookingId = payment.getBooking().getId();
            if (payment.getBooking().getProperty() != null) {
                this.propertyId = payment.getBooking().getProperty().getId();
                this.propertyTitle = payment.getBooking().getProperty().getTitle();
                this.propertyAddress = payment.getBooking().getProperty().getAddress();
                this.propertyCity = payment.getBooking().getProperty().getCity();
            }
            if (payment.getBooking().getTenant() != null) {
                this.tenantId = payment.getBooking().getTenant().getId();
                this.tenantName = payment.getBooking().getTenant().getName();
                this.tenantEmail = payment.getBooking().getTenant().getEmail();
                this.tenantPhone = payment.getBooking().getTenant().getPhone();
            }
            if (payment.getBooking().getOwner() != null) {
                this.ownerId = payment.getBooking().getOwner().getId();
                this.ownerName = payment.getBooking().getOwner().getName();
                this.ownerEmail = payment.getBooking().getOwner().getEmail();
            }
        }
        this.amount = payment.getAmount();
        this.paymentDate = payment.getPaymentDate();
        this.paymentStatus = payment.getPaymentStatus();
        this.transactionId = payment.getTransactionId();
        this.paymentMethod = payment.getPaymentMethod();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
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

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getPropertyCity() {
        return propertyCity;
    }

    public void setPropertyCity(String propertyCity) {
        this.propertyCity = propertyCity;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
