package com.example.interlink.queue.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QueueService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String QUEUE_KEY = "ticket:queue";
    private static final String USER_INFO_KEY_PREFIX = "ticket:user:";

    public void addToQueue(Long userId) {
        List<Object> queue = redisTemplate.opsForList().range(QUEUE_KEY, 0, -1);

        // 중복 추가 방지
        if (queue != null && queue.contains(userId.toString())) {
            System.out.println("이미 대기열에 추가된 사용자: " + userId);
            return;
        }

        // 대기열에 사용자 추가
        redisTemplate.opsForList().rightPush(QUEUE_KEY, userId.toString());

        // 사용자 정보 저장 (대기 시작 시간과 상태)
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("status", "WAITING"); // 상태: 대기 중
        userInfo.put("joinedAt", Instant.now().toString()); // 대기 시작 시간
        redisTemplate.opsForHash().putAll(USER_INFO_KEY_PREFIX + userId, userInfo);

        broadcastQueueState();
    }

    public Long removeFromQueue() {
        String userId = (String) redisTemplate.opsForList().leftPop(QUEUE_KEY);

        if (userId != null) {
            // 상태 변경: 처리 중
            redisTemplate.opsForHash().put(USER_INFO_KEY_PREFIX + userId, "status", "PROCESSING");
        }

        broadcastQueueState();
        return userId != null ? Long.parseLong(userId) : null;
    }

    public List<Object> getQueueState() {
        return redisTemplate.opsForList().range(QUEUE_KEY, 0, -1);
    }

    public Map<String, Object> getUserInfo(Long userId) {
        Map<Object, Object> rawUserInfo = redisTemplate.opsForHash().entries(USER_INFO_KEY_PREFIX + userId);

        // Object -> String으로 변환
        Map<String, Object> userInfo = new HashMap<>();
        for (Map.Entry<Object, Object> entry : rawUserInfo.entrySet()) {
            userInfo.put(entry.getKey().toString(), entry.getValue());
        }

        return userInfo;
    }

    private void broadcastQueueState() {
        List<Object> queue = getQueueState();
        Map<Long, Map<String, Object>> userDetails = new HashMap<>();

        // 대기열 사용자들의 상세 정보 추가
        if (queue != null) {
            for (Object userId : queue) {
                Long id = Long.parseLong(userId.toString());
                userDetails.put(id, getUserInfo(id));
            }
        }

        messagingTemplate.convertAndSend("/topic/queue/1", userDetails); // WebSocket 브로드캐스트
        System.out.println("대기열 브로드캐스트 상태: " + userDetails);
    }
}
