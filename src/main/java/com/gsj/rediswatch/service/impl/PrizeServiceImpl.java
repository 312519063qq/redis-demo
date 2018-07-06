package com.gsj.rediswatch.service.impl;

import com.gsj.rediswatch.mapper.PrizeDetailMapper;
import com.gsj.rediswatch.mapper.PrizeMapper;
import com.gsj.rediswatch.model.Prize;
import com.gsj.rediswatch.redis.RedisClient;
import com.gsj.rediswatch.service.PrizeService;
import com.gsj.rediswatch.utils.common.AppJsonObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrizeServiceImpl implements PrizeService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisClient redisClient;

    private static  final String WATCH_KEY="watchKey";



    @Override
    public AppJsonObj updateWatched() {
        AppJsonObj obj = new AppJsonObj();
        Object o =  redisClient.execForNum(WATCH_KEY,-1);
        System.out.println("Object："+o==null);
        obj.setData(redisClient.get(WATCH_KEY));
         return obj;
    }

    @Override
    public AppJsonObj mq(String topic,String var) {
        AppJsonObj obj =new AppJsonObj();
        Long length =  redisClient.llen(topic);
        if(length==10){
            obj.setData("已经抢购完毕");
            return obj;
        }
        Long l1  = redisClient.rpush(topic,var);
        if(l1>0){
            redisClient.pushMessage(topic,var);
        }
        obj.setData(l1);
        return obj;
    }

    @Override
    public AppJsonObj kill() throws InterruptedException {
        AppJsonObj obj = new AppJsonObj();
        Map<String,String> msg = new HashMap<>();

        Integer overNum = Integer.valueOf(redisClient.get(WATCH_KEY));
        if(overNum<=0){
            msg.put("message","商品已卖完！");
            obj.setMeta(msg);
            return obj;
        }
        List<Object> o = (List<Object>) redisTemplate.execute(new SessionCallback() {
            @Nullable
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.watch(WATCH_KEY);
                redisOperations.multi();
                redisOperations.opsForValue().increment(WATCH_KEY,-1);
//                try {
//                    System.out.println("休眠  模拟事物没执行之前修改 watched数据 调 ");
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                return redisOperations.exec();
            }
        });
        if(o==null || o.size()==0){
            System.out.println("抢购失败--->num="+redisClient.get(WATCH_KEY));
            msg.put("num",redisClient.get(WATCH_KEY));
            msg.put("message","抢购失败！");
            obj.setMeta(msg);
            return obj;
        }else{
            System.out.println("抢购成功----->>>>>>>>-->num"+redisClient.get(WATCH_KEY));
            msg.put("message","抢购成功！");
            msg.put("num",redisClient.get(WATCH_KEY));
            obj.setMeta(msg);
            return obj;
        }
    }
}
