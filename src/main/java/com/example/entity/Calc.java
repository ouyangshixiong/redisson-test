package com.example.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonAtomicLong;
import org.redisson.api.RAtomicLong;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author alexouyang
 * @Date 2019-10-31
 */
@REntity
@Data
@Slf4j
public abstract class Calc implements Serializable {

    @RId
    String name;

//    AtomicInteger count = new AtomicInteger(0);
    int count = 0;

    public synchronized void increasePrimary(){
        this.count++;
    }

    public synchronized void increaseByMethod(){
        setCount(getCount()+1);
    }



}
