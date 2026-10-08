package com.propertyrental.service;

import com.propertyrental.dto.NotificationResponseDTO;
import com.propertyrental.entity.Notification;
import com.propertyrental.entity.User;
import com.propertyrental.exception.ResourceNotFoundException;
import com.propertyrental.exception.UnauthorizedException;
import com.propertyrental.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    @Lazy
    private AuthService authService;

    @Transactional
    public void createNotification(User user, String message, String type) {
        Notification notification = new Notification(user, message, type);
        notificationRepository.save(notification);
    }

    public List<NotificationResponseDTO> getUserNotifications() {
        User currentUser = authService.getCurrentAuthenticatedUser();
        return notificationRepository.findByUserOrderByCreatedAtDesc(currentUser).stream()
                .map(NotificationResponseDTO::new)
                .collect(Collectors.toList());
    }

    public long getUnreadCount() {
        User currentUser = authService.getCurrentAuthenticatedUser();
        return notificationRepository.countByUserAndIsReadFalse(currentUser);
    }

    @Transactional
    public NotificationResponseDTO markAsRead(Long id) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));

        if (!notification.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not authorized to view this notification");
        }

        notification.setRead(true);
        Notification saved = notificationRepository.save(notification);
        return new NotificationResponseDTO(saved);
    }

    @Transactional
    public void markAllAsRead() {
        User currentUser = authService.getCurrentAuthenticatedUser();
        List<Notification> list = notificationRepository.findByUserOrderByCreatedAtDesc(currentUser);
        for (Notification n : list) {
            n.setRead(true);
        }
        notificationRepository.saveAll(list);
    }
}
