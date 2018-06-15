package com.gsj.rediswatch.mapper;

import com.gsj.rediswatch.model.PrizeDetail;

public interface PrizeDetailMapper {
    int insert(PrizeDetail record);

    int insertSelective(PrizeDetail record);

    int update();
}