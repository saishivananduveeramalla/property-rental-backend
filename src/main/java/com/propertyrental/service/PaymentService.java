package com.propertyrental.service;

import com.propertyrental.dto.PaymentRequestDTO;
import com.propertyrental.dto.PaymentResponseDTO;
import com.propertyrental.entity.*;
import com.propertyrental.exception.BadRequestException;
import com.propertyrental.exception.ResourceNotFoundException;
import com.propertyrental.exception.UnauthorizedException;
import com.propertyrental.repository.BookingRepository;
import com.propertyrental.repository.PaymentRepository;
import com.propertyrental.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuthService authService;

    @Transactional
    public PaymentResponseDTO processDemoPayment(PaymentRequestDTO dto) {
        User currentUser = authService.getCurrentAuthenticatedUser();

        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + dto.getBookingId()));

        if (!booking.getTenant().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("Only the tenant associated with this booking can make the payment");
        }

        if (booking.getStatus() != BookingStatus.APPROVED) {
            throw new BadRequestException("Payment can only be made for APPROVED bookings. Current status: " + booking.getStatus());
        }

        if (paymentRepository.findFirstByBookingAndPaymentStatus(booking, PaymentStatus.PAID).isPresent()) {
            throw new BadRequestException("This booking has already been paid for");
        }

        String transactionId = "TXN-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String method = (dto.getPaymentMethod() != null && !dto.getPaymentMethod().trim().isEmpty())
                ? dto.getPaymentMethod().toUpperCase()
                : "CARD";

        Payment payment = new Payment(
                booking,
                booking.getTotalAmount(),
                PaymentStatus.PAID,
                transactionId,
                method
        );

        Payment savedPayment = paymentRepository.save(payment);

        // Mark booking as COMPLETED/CONFIRMED
        booking.setStatus(BookingStatus.COMPLETED);
        bookingRepository.save(booking);

        // Mark property as currently rented (unavailable)
        Property property = booking.getProperty();
        property.setAvailable(false);
        propertyRepository.save(property);

        // Send notifications
        notificationService.createNotification(
                booking.getTenant(),
                "Payment of ₹" + String.format("%.2f", payment.getAmount()) + " successful! Rental for \"" + property.getTitle() + "\" is confirmed. Transaction ID: " + transactionId,
                "PAYMENT"
        );

        notificationService.createNotification(
                booking.getOwner(),
                "Rent payment of ₹" + String.format("%.2f", payment.getAmount()) + " received from " + currentUser.getName() + " for property: " + property.getTitle() + ". Booking is now confirmed!",
                "PAYMENT"
        );

        return new PaymentResponseDTO(savedPayment);
    }

    public PaymentResponseDTO getPaymentById(Long id) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment record not found with id: " + id));

        Booking booking = payment.getBooking();
        if (!booking.getTenant().getId().equals(currentUser.getId()) &&
            !booking.getOwner().getId().equals(currentUser.getId()) &&
            currentUser.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("You are not authorized to view this payment");
        }

        return new PaymentResponseDTO(payment);
    }

    public List<PaymentResponseDTO> getPaymentsForBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        return paymentRepository.findByBooking(booking).stream()
                .map(PaymentResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAllByOrderByPaymentDateDesc().stream()
                .map(PaymentResponseDTO::new)
                .collect(Collectors.toList());
    }
}
