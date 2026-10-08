package com.propertyrental.service;

import com.propertyrental.dto.MessageRequestDTO;
import com.propertyrental.dto.MessageResponseDTO;
import com.propertyrental.dto.ReplyRequestDTO;
import com.propertyrental.entity.ContactMessage;
import com.propertyrental.entity.Property;
import com.propertyrental.entity.User;
import com.propertyrental.exception.BadRequestException;
import com.propertyrental.exception.ResourceNotFoundException;
import com.propertyrental.exception.UnauthorizedException;
import com.propertyrental.repository.ContactMessageRepository;
import com.propertyrental.repository.PropertyRepository;
import com.propertyrental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuthService authService;

    @Transactional
    public MessageResponseDTO sendMessage(MessageRequestDTO dto) {
        User sender = authService.getCurrentAuthenticatedUser();
        User receiver = null;
        Property property = null;

        if (dto.getPropertyId() != null) {
            property = propertyRepository.findById(dto.getPropertyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + dto.getPropertyId()));
            receiver = property.getOwner();
        } else if (dto.getReceiverId() != null) {
            receiver = userRepository.findById(dto.getReceiverId())
                    .orElseThrow(() -> new ResourceNotFoundException("Receiver not found with id: " + dto.getReceiverId()));
        } else {
            throw new BadRequestException("Either propertyId or receiverId must be provided");
        }

        if (receiver.getId().equals(sender.getId())) {
            throw new BadRequestException("You cannot send a message to yourself");
        }

        ContactMessage msg = new ContactMessage(sender, receiver, property, dto.getMessage());
        ContactMessage saved = contactMessageRepository.save(msg);

        // Notify receiver
        notificationService.createNotification(
                receiver,
                "New message from " + sender.getName() + (property != null ? " regarding \"" + property.getTitle() + "\"" : ""),
                "MESSAGE"
        );

        return new MessageResponseDTO(saved);
    }

    @Transactional
    public MessageResponseDTO replyMessage(Long messageId, ReplyRequestDTO dto) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        ContactMessage msg = contactMessageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + messageId));

        if (!msg.getReceiver().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Only the message recipient can reply to this message");
        }

        msg.setReply(dto.getReply());
        msg.setRepliedAt(LocalDateTime.now());
        ContactMessage saved = contactMessageRepository.save(msg);

        // Notify original sender
        notificationService.createNotification(
                msg.getSender(),
                currentUser.getName() + " replied to your inquiry: \"" + (dto.getReply().length() > 50 ? dto.getReply().substring(0, 50) + "..." : dto.getReply()) + "\"",
                "MESSAGE"
        );

        return new MessageResponseDTO(saved);
    }

    public List<MessageResponseDTO> getMyReceivedMessages() {
        User currentUser = authService.getCurrentAuthenticatedUser();
        return contactMessageRepository.findByReceiverOrderBySentAtDesc(currentUser).stream()
                .map(MessageResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<MessageResponseDTO> getMySentMessages() {
        User currentUser = authService.getCurrentAuthenticatedUser();
        return contactMessageRepository.findBySenderOrderBySentAtDesc(currentUser).stream()
                .map(MessageResponseDTO::new)
                .collect(Collectors.toList());
    }
}
