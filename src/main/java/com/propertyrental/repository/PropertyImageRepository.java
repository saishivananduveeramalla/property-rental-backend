package com.propertyrental.repository;

import com.propertyrental.entity.Property;
import com.propertyrental.entity.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long> {
    List<PropertyImage> findByProperty(Property property);
    void deleteByProperty(Property property);
}
