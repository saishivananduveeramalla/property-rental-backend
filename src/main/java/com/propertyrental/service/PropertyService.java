package com.propertyrental.service;

import com.propertyrental.dto.PropertyRequestDTO;
import com.propertyrental.dto.PropertyResponseDTO;
import com.propertyrental.entity.Property;
import com.propertyrental.entity.PropertyImage;
import com.propertyrental.entity.PropertyType;
import com.propertyrental.entity.Role;
import com.propertyrental.entity.User;
import com.propertyrental.exception.BadRequestException;
import com.propertyrental.exception.ResourceNotFoundException;
import com.propertyrental.exception.UnauthorizedException;
import com.propertyrental.repository.PropertyImageRepository;
import com.propertyrental.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyImageRepository propertyImageRepository;

    @Autowired
    private AuthService authService;

    public List<PropertyResponseDTO> getAllProperties() {
        return propertyRepository.findAll().stream()
                .map(PropertyResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<PropertyResponseDTO> getAvailableProperties() {
        return propertyRepository.findByAvailableTrue().stream()
                .map(PropertyResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<PropertyResponseDTO> searchAndFilter(
            String city, PropertyType propertyType, Double minRent, Double maxRent,
            Integer bedrooms, Boolean available, String sortBy) {

        List<Property> list = propertyRepository.searchAndFilter(
                (city != null && !city.trim().isEmpty()) ? city.trim() : null,
                propertyType,
                minRent,
                maxRent,
                bedrooms,
                available
        );

        // Sorting options: rent_asc, rent_desc, date_desc
        if ("rent_asc".equalsIgnoreCase(sortBy)) {
            list.sort(Comparator.comparing(Property::getRent));
        } else if ("rent_desc".equalsIgnoreCase(sortBy)) {
            list.sort(Comparator.comparing(Property::getRent).reversed());
        } else {
            list.sort(Comparator.comparing(Property::getCreatedAt).reversed());
        }

        return list.stream().map(PropertyResponseDTO::new).collect(Collectors.toList());
    }

    public PropertyResponseDTO getPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
        return new PropertyResponseDTO(property);
    }

    public List<PropertyResponseDTO> getPropertiesByOwner(Long ownerId) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        if (currentUser.getRole() != Role.ROLE_ADMIN && !currentUser.getId().equals(ownerId)) {
            throw new UnauthorizedException("You are not authorized to view another owner's properties");
        }

        return propertyRepository.findByOwner(currentUser).stream()
                .map(PropertyResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PropertyResponseDTO createProperty(PropertyRequestDTO dto) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        if (currentUser.getRole() != Role.ROLE_OWNER && currentUser.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("Only property owners or admins can list properties");
        }

        Property property = new Property(
                dto.getTitle(),
                dto.getDescription(),
                dto.getPropertyType(),
                dto.getAddress(),
                dto.getCity(),
                dto.getState(),
                dto.getPincode(),
                dto.getRent(),
                dto.getBedrooms(),
                dto.getBathrooms(),
                dto.getArea(),
                dto.getFurnishedStatus(),
                dto.getAmenities(),
                dto.isAvailable(),
                currentUser
        );

        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            for (String imgUrl : dto.getImages()) {
                if (imgUrl != null && !imgUrl.trim().isEmpty()) {
                    property.addImage(imgUrl.trim());
                }
            }
        }

        Property saved = propertyRepository.save(property);
        return new PropertyResponseDTO(saved);
    }

    @Transactional
    public PropertyResponseDTO updateProperty(Long id, PropertyRequestDTO dto) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));

        if (!property.getOwner().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("You do not have permission to modify this property");
        }

        property.setTitle(dto.getTitle());
        property.setDescription(dto.getDescription());
        property.setPropertyType(dto.getPropertyType());
        property.setAddress(dto.getAddress());
        property.setCity(dto.getCity());
        property.setState(dto.getState());
        property.setPincode(dto.getPincode());
        property.setRent(dto.getRent());
        property.setBedrooms(dto.getBedrooms());
        property.setBathrooms(dto.getBathrooms());
        property.setArea(dto.getArea());
        property.setFurnishedStatus(dto.getFurnishedStatus());
        property.setAmenities(dto.getAmenities());
        property.setAvailable(dto.isAvailable());

        if (dto.getImages() != null) {
            property.getImages().clear();
            for (String imgUrl : dto.getImages()) {
                if (imgUrl != null && !imgUrl.trim().isEmpty()) {
                    property.addImage(imgUrl.trim());
                }
            }
        }

        Property updated = propertyRepository.save(property);
        return new PropertyResponseDTO(updated);
    }

    @Transactional
    public PropertyResponseDTO toggleAvailability(Long id) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));

        if (!property.getOwner().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("You do not have permission to modify this property");
        }

        property.setAvailable(!property.isAvailable());
        Property updated = propertyRepository.save(property);
        return new PropertyResponseDTO(updated);
    }

    @Transactional
    public void deleteProperty(Long id) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));

        if (!property.getOwner().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedException("You do not have permission to delete this property");
        }

        propertyRepository.delete(property);
    }
}
