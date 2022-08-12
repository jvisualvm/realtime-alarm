package com.risen.realtime.framework.util;

import com.risen.helper.constant.Symbol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/6/24 14:04
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    public Object getStrValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setStrValue(Object key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }


    public void setMapValue(Object mapKey, Object dataKey, Object value) {
        redisTemplate.opsForHash().put(mapKey, dataKey, value);
    }

    public Object getMapValue(Object mapKey, Object dataKey) {
        return redisTemplate.opsForHash().get(mapKey, dataKey);
    }


    public void publish(String topic, Object data) {
        redisTemplate.convertAndSend(topic, data);
    }


    public void addZset(String key, Object value, long score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }


    public Set getZsetByRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeByScore(key, start, end);
    }

    public long zsetCount(String key, long start, long end) {
        return redisTemplate.opsForZSet().count(key, start, end);
    }

    public long removeRangeByScore(String key, long start, long end) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, start, end);
    }

    public boolean deleteKey(String key) {
        return redisTemplate.delete(key);
    }

    public Long deleteCollectKey(List<String> collectKey) {
        return redisTemplate.delete(collectKey);
    }

    public Long deleteAllKeyByMatch(String keyLike) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(keyLike);
        keyBuilder.append(Symbol.SYMBOL_ALL);
        Set<String> keys = redisTemplate.keys(keyBuilder.toString());
        return redisTemplate.delete(keys);
    }

}
