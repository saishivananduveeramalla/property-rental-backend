package com.propertyrental.dto;

import jakarta.validation.constraints.NotBlank;

public class MessageRequestDTO {

    private Long receiverId;
    private Long propertyId;

    @NotBlank(message = "Message text cannot be empty")
    private String message;

    public MessageRequestDTO() {}

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
