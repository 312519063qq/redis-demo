package com.gsj.rediswatch.service.impl;

import com.gsj.rediswatch.mapper.PrizeDetailMapper;
import com.gsj.rediswatch.mapper.PrizeMapper;
import com.gsj.rediswatch.model.Prize;
import com.gsj.rediswatch.redis.RedisClient;
import com.gsj.rediswatch.service.PrizeService;
import com.gsj.rediswatch.utils.common.AppJsonObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private PrizeDetailMapper prizeDetailMapper;

    @Autowired
    private PrizeMapper prizeMapper;

    @Override
    public AppJsonObj prize(double weight) {
        AppJsonObj obj = new AppJsonObj();
        Map<String,String> msg = new HashMap<>();
        Integer prizeNum = Integer.valueOf(redisClient.get("prizeNum"));
        if(prizeNum>0){
            double random = Math.random();
            if(weight<random){
                redisClient.multi();
                Object o =  redisClient.execForNum("prizeNum",-1);
                if(o!=null){
                    prizeDetailMapper.update();
                    Prize prize = new Prize();
                    prize.setCreateTime(new Date());
                    prize.setPlatform(Integer.valueOf(redisClient.get("prizeNum")));
                    prizeMapper.insert(prize);
                    msg.put("message","中奖");
                }else{
                    msg.put("message","未中奖");
                }
            }else{
                msg.put("message","未中奖");
            }
        }else{
            msg.put("message","奖品已送完！！！！！！！！！");
        }
        obj.setMeta(msg);
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
}
