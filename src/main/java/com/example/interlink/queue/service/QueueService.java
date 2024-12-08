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
    private static final String WAITING_KEY = "ticket:waiting";
    private static final String USER_INFO_KEY_PREFIX = "ticket:user:";
    private int maxTickets = 0;

    // 사용자 추가
    public void addToQueue(Long userId, int maxTickets) {
        System.out.println("addToQueue 호출됨: " + userId);

        List<Object> queue = getQueueState(QUEUE_KEY);
        List<Object> waiting = getQueueState(WAITING_KEY);

        if (queue.contains(userId.toString()) || waiting.contains(userId.toString())) {
            System.out.println("이미 대기열 또는 대기 상태에 있는 사용자: " + userId);
            return;
        }

        if (queue.size() < maxTickets) {
            redisTemplate.opsForList().rightPush(QUEUE_KEY, userId.toString());
            updateUserInfo(userId, "QUEUE");
            System.out.println("QUEUE에 추가됨: " + userId);

            // 바로 좌석 선택 페이지로 이동
            sendRedirectMessage(userId);
        } else {
            redisTemplate.opsForList().rightPush(WAITING_KEY, userId.toString());
            updateUserInfo(userId, "WAITING");
            System.out.println("WAITING에 추가됨: " + userId);
        }

        broadcastQueueState(maxTickets);
    }

    // 사용자 제거
    public void removeFromQueue() {
        System.out.println("removeFromQueue 호출됨");

        // 대기열에서 사용자 제거
        String userId = (String) redisTemplate.opsForList().leftPop(QUEUE_KEY);

        if (userId != null) {
            updateUserInfo(Long.parseLong(userId), "PROCESSING");

            String nextUserId = (String) redisTemplate.opsForList().leftPop(WAITING_KEY);
            if (nextUserId != null) {
                redisTemplate.opsForList().rightPush(QUEUE_KEY, nextUserId);
                updateUserInfo(Long.parseLong(nextUserId), "QUEUE");

                // 리다이렉트 명령 전송
                sendRedirectMessage(Long.parseLong(nextUserId));
            }

            broadcastQueueState(maxTickets);
        }
    }

    // 리다이렉트 메시지 전송 로직 분리
    private void sendRedirectMessage(Long userId) {
        messagingTemplate.convertAndSend(
                "/topic/user/" + userId,
                Map.of(
                        "action", "redirect",
                        "url", "/seat.html?userId=" + userId
                )
        );
        System.out.println("Redirect message sent to /topic/user/" + userId);
    }

    // 대기열 상태 반환
    public List<Object> getQueueState(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    // 사용자 정보 반환
    public Map<String, Object> getUserInfo(Long userId) {
        Map<Object, Object> rawUserInfo = redisTemplate.opsForHash().entries(getUserKey(userId));
        Map<String, Object> userInfo = new HashMap<>();
        rawUserInfo.forEach((key, value) -> userInfo.put(key.toString(), value));
        return userInfo;
    }

    // 사용자 상태 업데이트
    private void updateUserInfo(Long userId, String status) {
        String userKey = getUserKey(userId);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("status", status);
        userInfo.put("joinedAt", Instant.now().toString());
        redisTemplate.opsForHash().putAll(userKey, userInfo);
    }

    // 대기열 상태 브로드캐스트
    public void broadcastQueueState(int maxTickets) {
        List<Object> queue = getQueueState(QUEUE_KEY);
        List<Object> waiting = getQueueState(WAITING_KEY);

        Map<String, Object> queueDetails = new HashMap<>();
        queueDetails.put("queue", queue);
        queueDetails.put("waiting", waiting);
        queueDetails.put("maxTickets", maxTickets);

        messagingTemplate.convertAndSend("/topic/queue/1", queueDetails);
        System.out.println("대기열 상태 브로드캐스트: " + queueDetails);
    }

    // 사용자 대기 메시지
    public String getPositionMessage(Long userId, int maxTickets) {
        List<Object> queue = getQueueState(QUEUE_KEY);
        List<Object> waiting = getQueueState(WAITING_KEY);

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

    private String getUserKey(Long userId) {
        return USER_INFO_KEY_PREFIX + userId;
    }
}
