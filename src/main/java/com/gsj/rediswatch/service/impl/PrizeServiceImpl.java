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

//    @Autowired
//    private PrizeDetailMapper prizeDetailMapper;
//
//    @Autowired
//    private PrizeMapper prizeMapper;

    @Override
    public AppJsonObj prize(double weight) {
        AppJsonObj obj = new AppJsonObj();
        Map<String,String> msg = new HashMap<>();
        Integer prizeNum = Integer.valueOf(redisClient.get("prizeNum"));
        if(prizeNum>0){
            redisTemplate.watch("prizeNum");
            double random = Math.random();
//            if(weight<random){
                try {
                    System.out.println("睡眠10秒");
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                redisClient.multi();
                System.out.println("开始redis");
                Object o =  redisClient.execForNum("prizeNum",-1);
                System.out.println("Object："+o==null);
                if(o!=null){
//                    prizeDetailMapper.update();
                    Prize prize = new Prize();
                    prize.setCreateTime(new Date());
                    prize.setPlatform(Integer.valueOf(redisClient.get("prizeNum")));
//                    prizeMapper.insert(prize);
                    msg.put("message","中奖");
                }else{
                    msg.put("message","未中奖");
                }
//            }else{
//                msg.put("message","未中奖");
//            }
        }else{
            msg.put("message","奖品已送完！！！！！！！！！");
        }
        obj.setMeta(msg);
        return obj;
    }

    @Override
    public AppJsonObj watch() {
        AppJsonObj obj = new AppJsonObj();
        Object o =  redisClient.execForNum("killKey",-1);
        System.out.println("Object："+o==null);
        obj.setData(redisClient.get("killKey"));
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
        redisTemplate.watch("killKey");
        Integer overNum = Integer.valueOf(redisClient.get("killKey"));
        if(overNum<=0){
            msg.put("message","商品已卖完！");
            obj.setMeta(msg);
            return obj;
        }
        System.out.println("抢购前---------------->num="+overNum);
//        Thread.sleep(10000);
        Object o1  = redisClient.execForNum("killKey",-1);
        System.out.println("在监听后  事物前改动 watched key值---------------->num="+redisClient.get("killKey"));
        redisClient.multi();
        Object o  = redisClient.execForNum("killKey",-1);
        if(o==null){
            System.out.println("抢购失败--->num="+redisClient.get("killKey"));
            msg.put("num",redisClient.get("killKey"));
            msg.put("message","抢购失败！");
            obj.setMeta(msg);
            return obj;
        }else{
            System.out.println("抢购成功----->>>>>>>>-->num"+redisClient.get("killKey"));
            msg.put("message","抢购成功！");
            msg.put("num",redisClient.get("killKey"));
            obj.setMeta(msg);
            return obj;
        }
    }
}
