package com.gsj.rediswatch.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.List;

/**
 * @author Jayson
 * @date 2017/10/20
 */
@Service
public class RedisClient {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, String val) {
        redisTemplate.opsForValue().set(key, val);
    }

    public void set(String key, String val, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, val, timeout, unit);
    }

    public boolean isExistKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void  multi(){
        redisTemplate.multi();
    }

    public Object execForNum(String watched ,Integer num){
        SessionCallback sessionCallback = new SessionCallback() {
            @Nullable
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.multi();
                redisOperations.opsForValue().increment(watched,num);
                return redisOperations.exec();
            }
        };
        ListOperations<String, String> listOperations =  redisTemplate.opsForList();
        redisTemplate.execute(new RedisCallback(){

            @Nullable
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return null;
            }
        });
        return  redisTemplate.execute(sessionCallback);
    }

    public Long rpush(final String key, final String value) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte keys[] = getRedisMasterSerializer().serialize(key);
                byte values[] = getRedisMasterSerializer().serialize(value);
                return redisConnection.rPush(keys,values);
            }
        });
    }

    public Long llen(final String key) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte keys[] = getRedisMasterSerializer().serialize(key);
                return redisConnection.lLen(keys);
            }
        });
    }

    /**
     *  list列表类型:
     * 返回存储在key的列表里指定范围内的元素。Start和end偏移量都是基于0的下标，
     * 即list的第一个元素下标是0(list的开头)，第二个元素是下标1，以此类推。
     * 偏移量也可以是负数，表示偏移量是从list尾部开始计数。例如，-1表示列表的最后一个元素，-2是倒数第二个，以此类推。
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<byte[]> lrange(final String key, final Long start, final Long end) {
        return redisTemplate.execute(new RedisCallback<List<byte[]>>() {
            @Override
            public List<byte[]> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte keys[] = getRedisMasterSerializer().serialize(key);
                return redisConnection.lRange(keys,start,end);
            }
        });
    }

    protected RedisSerializer<String> getRedisMasterSerializer() {
        RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
        return redisSerializer;
    }


    public void pushMessage(String  topic,String message){
        redisTemplate.convertAndSend(topic,message);
    }
}
