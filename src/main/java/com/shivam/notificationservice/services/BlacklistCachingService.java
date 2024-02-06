package com.shivam.notificationservice.services;

import com.shivam.notificationservice.constants.Constants;
import com.shivam.notificationservice.exception.RepositoryException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class BlacklistCachingService {

    private RedisTemplate<String, String> redisTemplate;

    public void addToSet(String value) throws RepositoryException {
        try {
            redisTemplate.opsForSet().add(Constants.SET_KEY, value);
        } catch (Exception e){
            log.debug("redis cache set : adding new element threw error");
            throw new RepositoryException("redis cache threw error");
        }
    }

    public boolean isPresent(String value) throws RepositoryException {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(Constants.SET_KEY, value));
        } catch (Exception e) {
            log.debug("redis cache set : searching element threw error");
            throw new RepositoryException("redis cache threw error");
        }
    }

    public boolean removeFromSet(String value) throws RepositoryException {
        try {
            return redisTemplate.opsForSet().remove(Constants.SET_KEY, value) > 0;
        } catch (Exception e) {
            log.debug("redis cache set : removing element threw error");
            throw new RepositoryException("redis cache threw exception");
        }
    }

}