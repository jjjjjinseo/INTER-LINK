package com.example.interlink.websocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate; // WebSocket 메시지 전송

    public void notifyUser(Long userId, String message) {
        messagingTemplate.convertAndSend("/topic/queue/" + userId, message);
    }
}
