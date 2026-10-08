package com.propertyrental.repository;

import com.propertyrental.entity.ContactMessage;
import com.propertyrental.entity.Property;
import com.propertyrental.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    List<ContactMessage> findByReceiverOrderBySentAtDesc(User receiver);
    List<ContactMessage> findBySenderOrderBySentAtDesc(User sender);
    List<ContactMessage> findByPropertyOrderBySentAtDesc(Property property);
}
