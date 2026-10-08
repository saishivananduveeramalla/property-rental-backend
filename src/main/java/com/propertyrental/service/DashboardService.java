package com.propertyrental.service;

import com.propertyrental.dto.DashboardStatsDTO;
import com.propertyrental.entity.*;
import com.propertyrental.repository.BookingRepository;
import com.propertyrental.repository.PaymentRepository;
import com.propertyrental.repository.PropertyRepository;
import com.propertyrental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AuthService authService;

    public DashboardStatsDTO getAdminStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        stats.setTotalUsers(userRepository.count());
        stats.setTotalTenants(userRepository.countByRole(Role.ROLE_TENANT));
        stats.setTotalOwners(userRepository.countByRole(Role.ROLE_OWNER));

        stats.setTotalProperties(propertyRepository.count());
        stats.setAvailableProperties(propertyRepository.countByAvailableTrue());
        stats.setRentedProperties(propertyRepository.countByAvailableFalse());

        stats.setTotalBookings(bookingRepository.count());
        stats.setPendingRequests(bookingRepository.countByStatus(BookingStatus.PENDING));
        stats.setApprovedRentals(bookingRepository.countByStatus(BookingStatus.APPROVED));
        stats.setCompletedRentals(bookingRepository.countByStatus(BookingStatus.COMPLETED));

        Double revenue = paymentRepository.calculateTotalPaidAmount();
        stats.setTotalRevenue(revenue != null ? revenue : 0.0);

        // Groupings
        List<Property> allProperties = propertyRepository.findAll();
        Map<String, Long> byCity = allProperties.stream()
                .collect(Collectors.groupingBy(Property::getCity, Collectors.counting()));
        stats.setPropertiesByCity(byCity);

        Map<String, Long> byType = allProperties.stream()
                .collect(Collectors.groupingBy(p -> p.getPropertyType().name(), Collectors.counting()));
        stats.setPropertiesByType(byType);

        List<Booking> allBookings = bookingRepository.findAll();
        Map<String, Long> byStatus = allBookings.stream()
                .collect(Collectors.groupingBy(b -> b.getStatus().name(), Collectors.counting()));
        stats.setBookingsByStatus(byStatus);

        return stats;
    }

    public DashboardStatsDTO getOwnerStats() {
        User owner = authService.getCurrentAuthenticatedUser();
        DashboardStatsDTO stats = new DashboardStatsDTO();

        List<Property> ownerProperties = propertyRepository.findByOwner(owner);
        stats.setTotalProperties(ownerProperties.size());
        stats.setAvailableProperties(ownerProperties.stream().filter(Property::isAvailable).count());
        stats.setRentedProperties(ownerProperties.stream().filter(p -> !p.isAvailable()).count());

        stats.setTotalBookings(bookingRepository.countByOwner(owner));
        stats.setPendingRequests(bookingRepository.countByOwnerAndStatus(owner, BookingStatus.PENDING));
        stats.setApprovedRentals(bookingRepository.countByOwnerAndStatus(owner, BookingStatus.APPROVED));
        stats.setCompletedRentals(bookingRepository.countByOwnerAndStatus(owner, BookingStatus.COMPLETED));

        Double revenue = bookingRepository.calculateOwnerCompletedRevenue(owner);
        stats.setTotalRevenue(revenue != null ? revenue : 0.0);

        return stats;
    }

    public DashboardStatsDTO getTenantStats() {
        User tenant = authService.getCurrentAuthenticatedUser();
        DashboardStatsDTO stats = new DashboardStatsDTO();

        stats.setTotalBookings(bookingRepository.countByTenant(tenant));
        stats.setPendingRequests(bookingRepository.countByTenantAndStatus(tenant, BookingStatus.PENDING));
        stats.setApprovedRentals(bookingRepository.countByTenantAndStatus(tenant, BookingStatus.APPROVED));
        stats.setCompletedRentals(bookingRepository.countByTenantAndStatus(tenant, BookingStatus.COMPLETED));

        List<Booking> completed = bookingRepository.findByTenantOrderByCreatedAtDesc(tenant).stream()
                .filter(b -> b.getStatus() == BookingStatus.COMPLETED)
                .collect(Collectors.toList());
        double totalPaid = completed.stream().mapToDouble(Booking::getTotalAmount).sum();
        stats.setTotalRevenue(totalPaid);

        return stats;
    }
}
