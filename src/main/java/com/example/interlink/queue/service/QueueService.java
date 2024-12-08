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
    private int maxTickets = 0;


    public void addToQueue(Long userId, int maxTickets) {
        List<Object> queue = getQueueState(QUEUE_KEY);
        List<Object> waiting = getQueueState("ticket:waiting");

        if (queue != null && queue.contains(userId.toString())) {
            System.out.println("이미 대기열에 추가된 사용자: " + userId);
            return;
        }
        if (waiting != null && waiting.contains(userId.toString())) {
            System.out.println("이미 대기 상태에 있는 사용자: " + userId);
            return;
        }

        if (queue != null && queue.size() < maxTickets) {
            redisTemplate.opsForList().rightPush(QUEUE_KEY, userId.toString());
            System.out.println("사용자 추가됨 (대기열): " + userId);
        } else {
            redisTemplate.opsForList().rightPush("ticket:waiting", userId.toString());
            System.out.println("사용자 추가됨 (대기 상태): " + userId);
        }

        String userKey = getUserKey(userId);
        if (!redisTemplate.hasKey(userKey)) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("status", queue != null && queue.size() < maxTickets ? "QUEUE" : "WAITING");
            userInfo.put("joinedAt", Instant.now().toString());
            redisTemplate.opsForHash().putAll(userKey, userInfo);
        }

        broadcastQueueState(maxTickets);
    }

    public Long removeFromQueue() {
        String userId = (String) redisTemplate.opsForList().leftPop(QUEUE_KEY);

        if (userId != null) {
            redisTemplate.opsForHash().put(getUserKey(Long.parseLong(userId)), "status", "PROCESSING");
        }

        broadcastQueueState(maxTickets);
        return userId != null ? Long.parseLong(userId) : null;
    }

    public List<Object> getQueueState(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public Map<String, Object> getUserInfo(Long userId) {
        Map<Object, Object> rawUserInfo = redisTemplate.opsForHash().entries(getUserKey(userId));

        Map<String, Object> userInfo = new HashMap<>();
        for (Map.Entry<Object, Object> entry : rawUserInfo.entrySet()) {
            userInfo.put(entry.getKey().toString(), entry.getValue());
        }

        return userInfo;
    }

    private void broadcastQueueState(int maxTickets) {
        List<Object> queue = getQueueState(QUEUE_KEY);
        List<Object> waiting = getQueueState("ticket:waiting");

        Map<String, Object> queueDetails = new HashMap<>();
        queueDetails.put("queue", queue);
        queueDetails.put("waiting", waiting);
        queueDetails.put("maxTickets", maxTickets);

        messagingTemplate.convertAndSend("/topic/queue/1", queueDetails);
        System.out.println("대기열 브로드캐스트 상태: " + queueDetails);
    }

    private String getUserKey(Long userId) {
        return USER_INFO_KEY_PREFIX + userId;
    }

    public String getPositionMessage(Long userId, int maxTickets) {
        List<Object> queue = getQueueState(QUEUE_KEY);
        List<Object> waiting = getQueueState("ticket:waiting");

        int positionInQueue = queue.indexOf(userId.toString());
        int positionInWaiting = waiting.indexOf(userId.toString());

        if (positionInQueue >= 0) {
            return String.format("대기열에 추가되었습니다. 현재 순서: %d", positionInQueue + 1);
        } else if (positionInWaiting >= 0) {
            return String.format("대기 상태로 전환되었습니다. 내 앞에 %d명 남았습니다.", positionInWaiting);
        } else {
            return "대기열 또는 대기 상태에 추가되지 않았습니다.";
        }
    }
}
