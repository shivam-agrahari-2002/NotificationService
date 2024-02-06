package com.shivam.notificationservice.services;

import com.shivam.notificationservice.constants.Constants;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BlacklistCachingService {

    private RedisTemplate<String, String> redisTemplate;

    public void addToSet(String value) {
        redisTemplate.opsForSet().add(Constants.SET_KEY, value);
    }

    public boolean isPresent(String value){
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(Constants.SET_KEY, value));
    }

    public boolean removeFromSet(String value) {
        return redisTemplate.opsForSet().remove(Constants.SET_KEY, value) > 0;
    }

}