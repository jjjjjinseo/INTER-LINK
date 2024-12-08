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
    public ResponseEntity<String> enterQueue(@RequestParam Long userId, @RequestParam int maxTickets) {
        queueService.addToQueue(userId, maxTickets);  // 대기열에 사용자 추가
        String positionMessage = queueService.getPositionMessage(userId, maxTickets);  // 대기열 메시지 생성
        return ResponseEntity.ok(positionMessage);  // 응답 메시지 반환
    }

    @GetMapping("/state")
    public ResponseEntity<?> getQueueState() {
        return ResponseEntity.ok(queueService.getQueueState("ticket:queue"));  // 대기열 상태 반환
    }

    @GetMapping("/waiting")
    public ResponseEntity<?> getWaitingState() {
        return ResponseEntity.ok(queueService.getQueueState("ticket:waiting"));  // 대기 상태 반환
    }
}
