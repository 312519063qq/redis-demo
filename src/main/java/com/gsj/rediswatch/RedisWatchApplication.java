package com.gsj.rediswatch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.gsj")
@MapperScan("com.gsj.rediswatch.mapper")
public class RedisWatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisWatchApplication.class, args);
	}
}
