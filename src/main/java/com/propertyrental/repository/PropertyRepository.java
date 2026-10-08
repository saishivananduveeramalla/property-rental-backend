package com.propertyrental.repository;

import com.propertyrental.entity.Property;
import com.propertyrental.entity.PropertyType;
import com.propertyrental.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByOwner(User owner);

    List<Property> findByAvailableTrue();

    long countByAvailableTrue();

    long countByAvailableFalse();

    @Query("SELECT p FROM Property p WHERE " +
           "(:city IS NULL OR LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%')) OR LOWER(p.address) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
           "(:propertyType IS NULL OR p.propertyType = :propertyType) AND " +
           "(:minRent IS NULL OR p.rent >= :minRent) AND " +
           "(:maxRent IS NULL OR p.rent <= :maxRent) AND " +
           "(:bedrooms IS NULL OR p.bedrooms >= :bedrooms) AND " +
           "(:available IS NULL OR p.available = :available)")
    List<Property> searchAndFilter(
            @Param("city") String city,
            @Param("propertyType") PropertyType propertyType,
            @Param("minRent") Double minRent,
            @Param("maxRent") Double maxRent,
            @Param("bedrooms") Integer bedrooms,
            @Param("available") Boolean available
    );
}
