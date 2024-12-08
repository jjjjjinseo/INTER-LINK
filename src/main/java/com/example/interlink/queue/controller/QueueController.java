package com.example.interlink.queue.controller;

import com.example.interlink.queue.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/queue")
public class QueueController {
    private final QueueService queueService;

    @PostMapping("/enter")
    public ResponseEntity<String> enterQueue(@RequestParam Long userId) {
        queueService.addToQueue(userId);
        return ResponseEntity.ok("대기열에 추가되었습니다. 사용자 ID: " + userId);
    }

    @GetMapping("/state")
    public ResponseEntity<?> getQueueState() {
        return ResponseEntity.ok(queueService.getQueueState());
    }
}
