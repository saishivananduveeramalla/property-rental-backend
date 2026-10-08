package com.propertyrental.dto;

import com.propertyrental.entity.ContactMessage;
import java.time.LocalDateTime;

public class MessageResponseDTO {
    private Long id;
    private Long senderId;
    private String senderName;
    private String senderEmail;
    private String senderPhone;

    private Long receiverId;
    private String receiverName;
    private String receiverEmail;

    private Long propertyId;
    private String propertyTitle;

    private String message;
    private String reply;
    private LocalDateTime sentAt;
    private LocalDateTime repliedAt;

    public MessageResponseDTO() {}

    public MessageResponseDTO(ContactMessage msg) {
        this.id = msg.getId();
        if (msg.getSender() != null) {
            this.senderId = msg.getSender().getId();
            this.senderName = msg.getSender().getName();
            this.senderEmail = msg.getSender().getEmail();
            this.senderPhone = msg.getSender().getPhone();
        }
        if (msg.getReceiver() != null) {
            this.receiverId = msg.getReceiver().getId();
            this.receiverName = msg.getReceiver().getName();
            this.receiverEmail = msg.getReceiver().getEmail();
        }
        if (msg.getProperty() != null) {
            this.propertyId = msg.getProperty().getId();
            this.propertyTitle = msg.getProperty().getTitle();
        }
        this.message = msg.getMessage();
        this.reply = msg.getReply();
        this.sentAt = msg.getSentAt();
        this.repliedAt = msg.getRepliedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyTitle() {
        return propertyTitle;
    }

    public void setPropertyTitle(String propertyTitle) {
        this.propertyTitle = propertyTitle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public LocalDateTime getRepliedAt() {
        return repliedAt;
    }

    public void setRepliedAt(LocalDateTime repliedAt) {
        this.repliedAt = repliedAt;
    }
}
