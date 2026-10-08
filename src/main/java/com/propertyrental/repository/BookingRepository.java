package com.propertyrental.repository;

import com.propertyrental.entity.Booking;
import com.propertyrental.entity.BookingStatus;
import com.propertyrental.entity.Property;
import com.propertyrental.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByTenantOrderByCreatedAtDesc(User tenant);
    List<Booking> findByOwnerOrderByCreatedAtDesc(User owner);
    List<Booking> findByPropertyOrderByCreatedAtDesc(Property property);

    long countByStatus(BookingStatus status);
    long countByTenantAndStatus(User tenant, BookingStatus status);
    long countByOwnerAndStatus(User owner, BookingStatus status);
    long countByTenant(User tenant);
    long countByOwner(User owner);

    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.status = 'COMPLETED'")
    Double calculateTotalCompletedRevenue();

    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.owner = :owner AND b.status = 'COMPLETED'")
    Double calculateOwnerCompletedRevenue(User owner);
}
