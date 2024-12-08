package com.example.interlink.redis.controller;

import com.example.interlink.jwt.util.JwtUtil;
import com.example.interlink.redis.service.QueueService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/queue")
public class QueueController {
    private final QueueService queueService;
    private final JwtUtil jwtUtil;

    @PostMapping("/enter")
    public ResponseEntity<?> enterQueue(HttpServletRequest request) {
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        queueService.addToQueue(userId);
        return ResponseEntity.ok("대기열에 추가되었습니다.");
    }

    @PostMapping("/notify")
    public ResponseEntity<?> notifyQueue(HttpServletRequest request) {
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));

        if (!queueService.isUserNext(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("아직 대기열 순서가 아닙니다.");
        }

        queueService.removeFromQueue();
        return ResponseEntity.ok("대기열을 통과했습니다. 좌석을 선택하세요.");
    }

    @GetMapping("/position")
    public ResponseEntity<?> getQueuePosition(HttpServletRequest request) {
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        int position = queueService.getQueuePosition(userId);
        return ResponseEntity.ok("현재 대기열 순서: " + position);
    }
}
