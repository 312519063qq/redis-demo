package com.gsj.rediswatch.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxingjie on 17/7/13.
 */
@Configuration
public class RedisConfig {

    private String master = "mymaster";

    private String password = "";

    private List<String> sentinels = new ArrayList<String>() {{
        add("127.0.0.1:26379");
        //add("redis.test.com:26379");
    }};
    private List<PatternTopic> patternTopics=new ArrayList<PatternTopic>(){
        {
            add(new PatternTopic("topic"));
        }
    };

    //测试及线上用集群模式时，打开此段注释
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory(RedisSentinelConfiguration sentinelConfiguration) {
//        JedisConnectionFactory factory = new JedisConnectionFactory(sentinelConfiguration);
//        if (!StringUtils.isEmpty(password)) {
//            factory.setPassword(password);
//        }
//        return factory;
//    }
//
//    @Bean
//    public RedisSentinelConfiguration sentinelConfig() {
//        RedisSentinelConfiguration configuration = new RedisSentinelConfiguration();
//        configuration.master(master);
//        for (String sentinel : sentinels) {
//            String[] hostAndPort = sentinel.split(":");
//            String host = hostAndPort[0];
//            int port = Integer.parseInt(hostAndPort[1]);
//            configuration.sentinel(host, port);
//        }
//        return configuration;
//    }

    //本地开发时，启动一个redis，用下面这个factory
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        if (!StringUtils.isEmpty(password)) {
            factory.setPassword(password);
        }
        return factory;
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(MessageReceiver messageReciver){
        return new MessageListenerAdapter(messageReciver,"receiveMessage");
    }
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory redisConnectionFactory,
                                                   MessageListenerAdapter messageListenerAdapter  ){
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        redisMessageListenerContainer.addMessageListener(messageListenerAdapter, patternTopics);
        return redisMessageListenerContainer;
    }


}
