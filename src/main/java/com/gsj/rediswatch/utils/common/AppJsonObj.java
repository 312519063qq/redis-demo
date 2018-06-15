package com.gsj.rediswatch.utils.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class AppJsonObj {

    public AppJsonObj() {
        Map<String,String> meta = new HashMap<String,String>();
        meta.put("message", "请求成功！");
        this.meta = meta;
    }

    Map<String,String> meta;

    Object data;
}
