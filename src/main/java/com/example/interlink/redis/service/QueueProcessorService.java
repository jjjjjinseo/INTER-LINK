package com.example.interlink.redis.service;

import com.example.interlink.websocket.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueProcessorService {
    private final QueueService queueService;
    private final NotificationService notificationService; // 사용자에게 알림
    private final RedisTemplate<String, Object> redisTemplate;

    @Scheduled(fixedRate = 5000)
    public void processQueue() {
        Long nextUser = queueService.removeFromQueue();
        if (nextUser != null) {
            notificationService.notifyUser(nextUser, "대기열을 통과했습니다. 좌석을 선택하세요.");
        }
    }
}
