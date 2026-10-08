package com.propertyrental.repository;

import com.propertyrental.entity.Booking;
import com.propertyrental.entity.Payment;
import com.propertyrental.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByBooking(Booking booking);
    Optional<Payment> findFirstByBookingAndPaymentStatus(Booking booking, PaymentStatus paymentStatus);
    Optional<Payment> findByTransactionId(String transactionId);
    List<Payment> findAllByOrderByPaymentDateDesc();

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentStatus = 'PAID'")
    Double calculateTotalPaidAmount();
}
