package com.example.interlink.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueueService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String QUEUE_KEY = "ticket:queue";

    public void addToQueue(Long userId) {
        redisTemplate.opsForList().rightPush(QUEUE_KEY, userId.toString());
    }

    public Long removeFromQueue() {
        String userId = (String) redisTemplate.opsForList().leftPop(QUEUE_KEY);
        return userId != null ? Long.parseLong(userId) : null;
    }

    public int getQueuePosition(Long userId) {
        List<Object> queue = redisTemplate.opsForList().range(QUEUE_KEY, 0, -1);
        return queue != null ? queue.indexOf(userId.toString()) + 1 : -1;
    }

    public boolean isUserNext(Long userId) {
        String nextUser = (String) redisTemplate.opsForList().index(QUEUE_KEY, 0);
        return nextUser != null && nextUser.equals(userId.toString());
    }
}
