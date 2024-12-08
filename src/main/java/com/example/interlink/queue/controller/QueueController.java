package com.example.interlink.queue.controller;

import com.example.interlink.queue.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/queue")
public class QueueController {
    private final QueueService queueService;

    @PostMapping("/enter")
    public ResponseEntity<Map<String, Object>> enterQueue(@RequestParam Long userId, @RequestParam int maxTickets) {
        queueService.addToQueue(userId, maxTickets); // 대기열에 사용자 추가
        String positionMessage = queueService.getPositionMessage(userId, maxTickets); // 대기열 메시지 생성

        // 사용자 상태 반환
        Map<String, Object> response = new HashMap<>();
        if (positionMessage.contains("대기열에 추가되었습니다")) {
            response.put("status", "QUEUE");
            response.put("url", "/seat.html?userId=" + userId);
        } else if (positionMessage.contains("대기 상태로 전환되었습니다")) {
            response.put("status", "WAITING");
            response.put("position", extractPosition(positionMessage)); // 대기 위치
        } else {
            response.put("status", "UNKNOWN");
            response.put("message", positionMessage);
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

    // 대기 위치 추출
    private int extractPosition(String message) {
        String[] splitMessage = message.split(" ");
        for (String part : splitMessage) {
            if (part.matches("\\d+")) { // 숫자인 부분만 추출
                return Integer.parseInt(part);
            }
        }
        return -1; // 추출 실패 시 -1 반환
    }
}
