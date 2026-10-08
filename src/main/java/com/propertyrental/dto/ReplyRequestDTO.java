package com.propertyrental.dto;

import jakarta.validation.constraints.NotBlank;

public class ReplyRequestDTO {

    @NotBlank(message = "Reply message cannot be empty")
    private String reply;

    public ReplyRequestDTO() {}

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
