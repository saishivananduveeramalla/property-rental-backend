package com.propertyrental.service;

import com.propertyrental.dto.BookingRequestDTO;
import com.propertyrental.dto.BookingResponseDTO;
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

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuthService authService;

    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::enrichBookingResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponseDTO> getTenantBookings() {
        User tenant = authService.getCurrentAuthenticatedUser();
        return bookingRepository.findByTenantOrderByCreatedAtDesc(tenant).stream()
                .map(this::enrichBookingResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponseDTO> getOwnerBookings() {
        User owner = authService.getCurrentAuthenticatedUser();
        return bookingRepository.findByOwnerOrderByCreatedAtDesc(owner).stream()
                .map(this::enrichBookingResponse)
                .collect(Collectors.toList());
    }

    public BookingResponseDTO getBookingById(Long id) {
        User user = authService.getCurrentAuthenticatedUser();
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking request not found with id: " + id));

        // Authorization check: tenant, owner, or admin
        if (!booking.getTenant().getId().equals(user.getId()) &&
            !booking.getOwner().getId().equals(user.getId()) &&
            user.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("You are not authorized to view this booking request");
        }

        return enrichBookingResponse(booking);
    }

    @Transactional
    public BookingResponseDTO createBookingRequest(BookingRequestDTO dto) {
        User tenant = authService.getCurrentAuthenticatedUser();
        if (tenant.getRole() != Role.ROLE_TENANT) {
            throw new BadRequestException("Only registered tenants can submit rental requests");
        }

        Property property = propertyRepository.findById(dto.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + dto.getPropertyId()));

        if (!property.isAvailable()) {
            throw new BadRequestException("This property is currently not available for rent");
        }

        if (property.getOwner().getId().equals(tenant.getId())) {
            throw new BadRequestException("Property owners cannot rent their own properties");
        }

        if (dto.getEndDate().isBefore(dto.getStartDate()) || dto.getEndDate().isEqual(dto.getStartDate())) {
            throw new BadRequestException("End date must be strictly after start date");
        }

        long days = ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate());
        if (days < 30) {
            throw new BadRequestException("Minimum rental duration is 30 days");
        }

        // Calculate total amount based on duration (approx months)
        double months = Math.max(1.0, (double) days / 30.0);
        double totalAmount = Math.round(property.getRent() * months);

        Booking booking = new Booking(
                property,
                tenant,
                property.getOwner(),
                dto.getStartDate(),
                dto.getEndDate(),
                property.getRent(),
                totalAmount,
                BookingStatus.PENDING,
                dto.getNotes()
        );

        Booking saved = bookingRepository.save(booking);

        // Notify owner
        notificationService.createNotification(
                property.getOwner(),
                "New rental request received from " + tenant.getName() + " for property: " + property.getTitle(),
                "REQUEST"
        );

        // Notify tenant confirmation
        notificationService.createNotification(
                tenant,
                "Your rental request for " + property.getTitle() + " has been submitted and is pending owner approval.",
                "REQUEST"
        );

        return enrichBookingResponse(saved);
    }

    @Transactional
    public BookingResponseDTO approveBooking(Long id) {
        User user = authService.getCurrentAuthenticatedUser();
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking request not found with id: " + id));

        if (!booking.getOwner().getId().equals(user.getId()) && user.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("Only the property owner or admin can approve this rental request");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BadRequestException("Only PENDING rental requests can be approved. Current status: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.APPROVED);
        Booking saved = bookingRepository.save(booking);

        // Notify tenant
        notificationService.createNotification(
                booking.getTenant(),
                "Good news! Your rental request for \"" + booking.getProperty().getTitle() + "\" has been APPROVED by the owner. Please proceed with payment to confirm.",
                "APPROVAL"
        );

        return enrichBookingResponse(saved);
    }

    @Transactional
    public BookingResponseDTO rejectBooking(Long id) {
        User user = authService.getCurrentAuthenticatedUser();
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking request not found with id: " + id));

        if (!booking.getOwner().getId().equals(user.getId()) && user.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("Only the property owner or admin can reject this rental request");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BadRequestException("Only PENDING rental requests can be rejected. Current status: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.REJECTED);
        Booking saved = bookingRepository.save(booking);

        // Notify tenant
        notificationService.createNotification(
                booking.getTenant(),
                "Your rental request for \"" + booking.getProperty().getTitle() + "\" was declined by the owner.",
                "REJECTION"
        );

        return enrichBookingResponse(saved);
    }

    @Transactional
    public BookingResponseDTO cancelBooking(Long id) {
        User user = authService.getCurrentAuthenticatedUser();
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking request not found with id: " + id));

        if (!booking.getTenant().getId().equals(user.getId()) && user.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("Only the tenant who created the request can cancel it");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BadRequestException("Only PENDING requests can be cancelled. Completed or already processed requests cannot be cancelled.");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking saved = bookingRepository.save(booking);

        // Notify owner
        notificationService.createNotification(
                booking.getOwner(),
                "The rental request for \"" + booking.getProperty().getTitle() + "\" was cancelled by tenant " + user.getName(),
                "CANCELLATION"
        );

        return enrichBookingResponse(saved);
    }

    private BookingResponseDTO enrichBookingResponse(Booking booking) {
        BookingResponseDTO dto = new BookingResponseDTO(booking);
        Optional<Payment> paymentOpt = paymentRepository.findFirstByBookingAndPaymentStatus(booking, PaymentStatus.PAID);
        if (paymentOpt.isPresent()) {
            dto.setPaid(true);
            dto.setTransactionId(paymentOpt.get().getTransactionId());
        } else {
            dto.setPaid(false);
        }
        return dto;
    }
}
