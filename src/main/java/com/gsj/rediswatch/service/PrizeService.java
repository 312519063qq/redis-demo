package com.gsj.rediswatch.service;

import com.gsj.rediswatch.utils.common.AppJsonObj;

public interface PrizeService {
    AppJsonObj prize(double weight);

    AppJsonObj watch();

    AppJsonObj mq(String topic,String value);


    AppJsonObj kill() throws InterruptedException;
}
