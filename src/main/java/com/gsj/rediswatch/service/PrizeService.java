package com.gsj.rediswatch.service;

import com.gsj.rediswatch.utils.common.AppJsonObj;

public interface PrizeService {
    AppJsonObj prize(double weight);

    AppJsonObj mq(String topic,String value);
}
