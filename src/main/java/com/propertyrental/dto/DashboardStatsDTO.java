package com.propertyrental.dto;

import java.util.Map;

public class DashboardStatsDTO {
    // Admin stats
    private long totalUsers;
    private long totalTenants;
    private long totalOwners;
    private long totalProperties;
    private long totalBookings;
    private long pendingRequests;
    private long approvedRentals;
    private long completedRentals;
    private long availableProperties;
    private long rentedProperties;
    private double totalRevenue;

    // Additional charts/distribution maps
    private Map<String, Long> propertiesByCity;
    private Map<String, Long> propertiesByType;
    private Map<String, Long> bookingsByStatus;

    public DashboardStatsDTO() {}

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalTenants() {
        return totalTenants;
    }

    public void setTotalTenants(long totalTenants) {
        this.totalTenants = totalTenants;
    }

    public long getTotalOwners() {
        return totalOwners;
    }

    public void setTotalOwners(long totalOwners) {
        this.totalOwners = totalOwners;
    }

    public long getTotalProperties() {
        return totalProperties;
    }

    public void setTotalProperties(long totalProperties) {
        this.totalProperties = totalProperties;
    }

    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public long getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(long pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public long getApprovedRentals() {
        return approvedRentals;
    }

    public void setApprovedRentals(long approvedRentals) {
        this.approvedRentals = approvedRentals;
    }

    public long getCompletedRentals() {
        return completedRentals;
    }

    public void setCompletedRentals(long completedRentals) {
        this.completedRentals = completedRentals;
    }

    public long getAvailableProperties() {
        return availableProperties;
    }

    public void setAvailableProperties(long availableProperties) {
        this.availableProperties = availableProperties;
    }

    public long getRentedProperties() {
        return rentedProperties;
    }

    public void setRentedProperties(long rentedProperties) {
        this.rentedProperties = rentedProperties;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Map<String, Long> getPropertiesByCity() {
        return propertiesByCity;
    }

    public void setPropertiesByCity(Map<String, Long> propertiesByCity) {
        this.propertiesByCity = propertiesByCity;
    }

    public Map<String, Long> getPropertiesByType() {
        return propertiesByType;
    }

    public void setPropertiesByType(Map<String, Long> propertiesByType) {
        this.propertiesByType = propertiesByType;
    }

    public Map<String, Long> getBookingsByStatus() {
        return bookingsByStatus;
    }

    public void setBookingsByStatus(Map<String, Long> bookingsByStatus) {
        this.bookingsByStatus = bookingsByStatus;
    }
}
