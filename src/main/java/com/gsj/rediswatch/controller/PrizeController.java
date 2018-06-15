package com.gsj.rediswatch.controller;

import com.gsj.rediswatch.redis.RedisClient;
import com.gsj.rediswatch.service.PrizeService;
import com.gsj.rediswatch.utils.common.AppJsonObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/test")
public class PrizeController {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private PrizeService prizeService;

    @RequestMapping("/prize")
    @ResponseBody
    public AppJsonObj prize(){
        return  prizeService.prize(Math.random());
    }

    @RequestMapping("/mq")
    @ResponseBody
    public AppJsonObj mq(String topic){
        return  prizeService.mq(topic, UUID.randomUUID().toString());
    }
}
