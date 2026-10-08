package com.propertyrental.controller;

import com.propertyrental.dto.BookingRequestDTO;
import com.propertyrental.dto.BookingResponseDTO;
import com.propertyrental.entity.Role;
import com.propertyrental.entity.User;
import com.propertyrental.service.AuthService;
import com.propertyrental.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private AuthService authService;

    @PostMapping
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<BookingResponseDTO> createBookingRequest(@Valid @RequestBody BookingRequestDTO dto) {
        return ResponseEntity.ok(bookingService.createBookingRequest(dto));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getBookings() {
        User currentUser = authService.getCurrentAuthenticatedUser();
        if (currentUser.getRole() == Role.ROLE_ADMIN) {
            return ResponseEntity.ok(bookingService.getAllBookings());
        } else if (currentUser.getRole() == Role.ROLE_OWNER) {
            return ResponseEntity.ok(bookingService.getOwnerBookings());
        } else {
            return ResponseEntity.ok(bookingService.getTenantBookings());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<BookingResponseDTO> approveBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.approveBooking(id));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<BookingResponseDTO> rejectBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.rejectBooking(id));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('TENANT', 'ADMIN')")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }
}
