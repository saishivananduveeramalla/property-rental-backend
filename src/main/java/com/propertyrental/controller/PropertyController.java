package com.propertyrental.controller;

import com.propertyrental.dto.PropertyRequestDTO;
import com.propertyrental.dto.PropertyResponseDTO;
import com.propertyrental.entity.PropertyType;
import com.propertyrental.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<PropertyResponseDTO>> getAllProperties() {
        return ResponseEntity.ok(propertyService.getAllProperties());
    }

    @GetMapping("/available")
    public ResponseEntity<List<PropertyResponseDTO>> getAvailableProperties() {
        return ResponseEntity.ok(propertyService.getAvailableProperties());
    }

    @GetMapping("/search")
    public ResponseEntity<List<PropertyResponseDTO>> searchProperties(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) PropertyType type,
            @RequestParam(required = false) Double minRent,
            @RequestParam(required = false) Double maxRent,
            @RequestParam(required = false) Integer bedrooms,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false, defaultValue = "date_desc") String sortBy) {

        return ResponseEntity.ok(propertyService.searchAndFilter(city, type, minRent, maxRent, bedrooms, available, sortBy));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponseDTO> getPropertyById(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.getPropertyById(id));
    }

    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<List<PropertyResponseDTO>> getPropertiesByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(propertyService.getPropertiesByOwner(ownerId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<PropertyResponseDTO> createProperty(@Valid @RequestBody PropertyRequestDTO dto) {
        return ResponseEntity.ok(propertyService.createProperty(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<PropertyResponseDTO> updateProperty(
            @PathVariable Long id,
            @Valid @RequestBody PropertyRequestDTO dto) {
        return ResponseEntity.ok(propertyService.updateProperty(id, dto));
    }

    @PutMapping("/{id}/toggle-availability")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<PropertyResponseDTO> toggleAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.toggleAvailability(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<Map<String, String>> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.ok(Map.of("message", "Property deleted successfully"));
    }
}
