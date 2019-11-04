package com.example.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author alexouyang
 * @Date 2019-10-31
 */
@REntity
@Data
@Slf4j
public abstract class Calc {

    @RId
    String name;

//    AtomicInteger count = new AtomicInteger(0);
    int count = 0;

    public void increasePrimary(){
        count++;
    }

    public void increaseByMethod(){
        setCount(getCount()+1);
    }


}
