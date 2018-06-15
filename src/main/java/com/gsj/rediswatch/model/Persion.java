package com.gsj.rediswatch.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Persion {

    private String firstName, lastName, job, gender;
    private int age, salary;
}
