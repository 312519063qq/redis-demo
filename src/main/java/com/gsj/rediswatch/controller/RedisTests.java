package com.gsj.rediswatch.controller;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedisTests {
    public static void main(String[] args) {
        final String watchkeys = "key";
        ExecutorService executor = Executors.newFixedThreadPool(20);

        for (int i = 0; i < 10000; i++) {// 测试一万人同时访问
            Jedis jedis = new Jedis("127.0.0.1", 6379);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        jedis.watch(watchkeys);// watchkeys
                        String val = jedis.get(watchkeys);
                        int valint = Integer.valueOf(val);
                        String userifo = UUID.randomUUID().toString();
                        if (valint < 10) {
                            Transaction tx = jedis.multi();// 开启事务
                            tx.incr(watchkeys);
                            List<Object> list = tx.exec();// 提交事务，如果此时watchkeys被改动了，则返回null
                            if (list != null && list.size()>0) {
                                System.out.println("用户：" + userifo + "抢购成功，当前抢购成功人数:" + (valint + 1));
                            } else {
                                System.out.println("用户：" + userifo + "抢购失败--------------并发");
                            }

                        } else {
                            System.out.println("用户：" + userifo + "抢购失败");
                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        jedis.close();
                    }
                }
            });
        }
        executor.shutdown();
    }
}
