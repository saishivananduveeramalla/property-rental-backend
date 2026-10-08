package com.propertyrental.controller;

import com.propertyrental.dto.MessageRequestDTO;
import com.propertyrental.dto.MessageResponseDTO;
import com.propertyrental.dto.ReplyRequestDTO;
import com.propertyrental.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponseDTO> sendMessage(@Valid @RequestBody MessageRequestDTO dto) {
        return ResponseEntity.ok(messageService.sendMessage(dto));
    }

    @GetMapping("/received")
    public ResponseEntity<List<MessageResponseDTO>> getReceivedMessages() {
        return ResponseEntity.ok(messageService.getMyReceivedMessages());
    }

    @GetMapping("/sent")
    public ResponseEntity<List<MessageResponseDTO>> getSentMessages() {
        return ResponseEntity.ok(messageService.getMySentMessages());
    }

    @PutMapping("/{id}/reply")
    public ResponseEntity<MessageResponseDTO> replyMessage(
            @PathVariable Long id,
            @Valid @RequestBody ReplyRequestDTO dto) {
        return ResponseEntity.ok(messageService.replyMessage(id, dto));
    }
}
