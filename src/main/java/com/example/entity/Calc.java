package com.example.entity;

import com.example.controller.ApplicationContextProvider;
import com.example.controller.SnowFlake;
import com.example.service.RedissonService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBatch;
import org.redisson.api.RMap;
import org.redisson.api.RTransaction;

import java.io.Serializable;

/**
 * @author alexouyang
 * @Date 2019-10-31
 */
@Data
@Slf4j
public abstract class Calc implements Serializable {

    private long counterId = 0;

    public Calc(){

    }

    public Calc(RMap<Long,Long> counterMap){
        SnowFlake snowFlake = new SnowFlake(2,3);
        setCounterId(snowFlake.nextId());
        counterMap.put(getCounterId(), 0L);
    }

    public Calc(Long counterId){
        this.counterId = counterId;
    }


//    AtomicInteger count = new AtomicInteger(0);
//    int count = 0;

//    public synchronized void increasePrimary(){
//        this.count++;
//    }

    public void increaseByMethod(){

    }

    public void increase(){
        RedissonService rloClient = ApplicationContextProvider.getBean(RedissonService.class);
        rloClient.getAtomicLong("counter_" + getCounterId()).incrementAndGet();
    }

    public void increaseByBatch(RBatch batch){
        batch.getAtomicLong("counter_" + getCounterId()).incrementAndGetAsync();
    }

    public void increaseByTransaction(RTransaction transaction){
        RMap<Long,Long> counterMap = transaction.getMap("counterMap");
        counterMap.put(getCounterId(), counterMap.get(getCounterId())+1);
    }


    /**
     * 假装内部有个count对象
     * @return
     */
    public long queryCount(){
        RedissonService rloClient = ApplicationContextProvider.getBean(RedissonService.class);
//        return (Long)rloClient.getMap("counterMap").get(getCounterId());
        return rloClient.getAtomicLong("counter_" + getCounterId()).get();
    }

    /**
     * 假装内部有个count对象
     *
     */
    public void putCount(long count){
        RedissonService rloClient = ApplicationContextProvider.getBean(RedissonService.class);
//        rloClient.getMap("counterMap").put(getCounterId(), count);
        rloClient.getAtomicLong("counter_" + getCounterId()).set(count);
    }




}
