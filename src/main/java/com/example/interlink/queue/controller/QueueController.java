package com.example.interlink.queue.controller;

import com.example.interlink.queue.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/queue")
public class QueueController {
    private final QueueService queueService;

    @PostMapping("/enter")
    public ResponseEntity<Map<String, Object>> enterQueue(@RequestParam Long userId, @RequestParam int maxTickets) {
        queueService.addToQueue(userId, maxTickets); // 대기열에 사용자 추가

        List<Object> queue = queueService.getQueueState("ticket:queue");
        List<Object> waiting = queueService.getQueueState("ticket:waiting");

        Map<String, Object> response = new HashMap<>();
        if (queue.contains(userId.toString())) {
            response.put("status", "QUEUE");
            response.put("url", "/seat.html?userId=" + userId);
        } else if (waiting.contains(userId.toString())) {
            response.put("status", "WAITING");
            response.put("position", waiting.indexOf(userId.toString())); // 대기 위치 계산
        } else {
            response.put("status", "UNKNOWN");
            response.put("message", "사용자가 대기열 또는 대기 상태에 없습니다.");
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/state")
    public ResponseEntity<?> getQueueState() {
        return ResponseEntity.ok(queueService.getQueueState("ticket:queue")); // 대기열 상태 반환
    }

    @GetMapping("/waiting")
    public ResponseEntity<?> getWaitingState() {
        return ResponseEntity.ok(queueService.getQueueState("ticket:waiting")); // 대기 상태 반환
    }

}
