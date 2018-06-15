package com.gsj.rediswatch.mapper;

import com.gsj.rediswatch.model.Prize;

public interface PrizeMapper {
    int insert(Prize record);

    int insertSelective(Prize record);
}