package com.dateBuzz.backend.configuration;

import com.dateBuzz.backend.model.User;
import io.lettuce.core.RedisURI;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfiguration {

    //redis 설정을 yaml 파일 등으로 옮겼을 시 볼 수 있다.
//    private final RedisProperties redisProperties;
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        RedisURI redisURI = RedisURI.create(redisProperties.getUrl());
//        org.springframework.data.redis.connection.RedisConfiguration configuration = LettuceConnectionFactory.createRedisConfiguration(redisURI);
//        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration);
//        factory.afterPropertiesSet();
//        return factory;
//    }
//
//    @Bean
//    public RedisTemplate<String, User> userRedisTemplate() {
//        RedisTemplate<String, User> redisTemplate = new RedisTemplate<>();
////      1. redisTemplate 는 redis 명령어 정보, 명령어를 쉽게 쓸 수 있게 도와준
//        redisTemplate.setConnectionFactory(redisConnectionFactory()); // 레디스 서버의 정보를 담고 있는
//
////      2. serialize(key, value) 단계
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<User>(User.class));
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//
//        return redisTemplate;
//    }
}
