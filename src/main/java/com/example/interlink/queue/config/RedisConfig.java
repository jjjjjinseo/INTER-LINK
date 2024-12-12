package com.example.interlink.queue.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    // Redis 서버 연결을 위한 LettuceConnectionFactory 설정
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // Redis 서버 호스트와 포트 (A 인스턴스의 IP와 포트로 변경)
        String redisHost = "43.201.250.37"; // 예: "43.201.250.37"
        int redisPort = 6379;  // 기본 Redis 포트

        // LettuceConnectionFactory 생성
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    // RedisTemplate 설정
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);  // LettuceConnectionFactory 주입
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());  // 직렬화 방식 설정 (필요시 변경)
        return template;
    }
}
