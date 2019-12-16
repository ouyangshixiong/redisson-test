package com.example.controller;

import com.example.entity.Persist2;
import com.example.entity.SimGroup;
import com.example.entity.SimPool;
import com.example.entity.SimTest3;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 非级联模式
 * @author alexouyang
 * @Date 2019-12-11
 */
@RestController
@Slf4j
public class Persist2Controller {
    private final String redisKey = "persist2";

    RedissonClient redissonClient;

    RLiveObjectService rloClient;

    ConcurrentSkipListSet<SimPool> innerSet;

    public Persist2Controller( RedissonClient redissonClient ){
        this.redissonClient = redissonClient;
        rloClient = redissonClient.getLiveObjectService();
    }

    @RequestMapping("/persist2_init")
    public void persit2Init(){
        Persist2 persist2 = new Persist2();
        persist2.setName(redisKey);
        rloClient.persist(persist2);
    }

    @RequestMapping("/persist2_init_set")
    public void persit2InitSet(){
        innerSet = new ConcurrentSkipListSet<>();
//        RSet<SimPool> innerSet = redissonClient.getSet("innerSet");
        for (int i = 0; i < 3; i++) {
            SimPool simPool = new SimPool();
            simPool.setName("simPool_" + i);
            simPool.setId(i);
            simPool = rloClient.persist(simPool);
            innerSet.add(simPool);
        }

    }

    /**
     * 会失败
     */
    @RequestMapping("/persist2_in_memory")
    public void persit2InMemory(){
        Persist2 persist2 = new Persist2();
        persist2.setName(redisKey);
        ConcurrentSkipListSet<SimPool> temp = new ConcurrentSkipListSet<>(Comparator.naturalOrder());
        for (int i = 0; i < 5; i++) {
            SimPool simPool = new SimPool();
            simPool.setName("simPool_" + i);
            simPool.setId(i);
            temp.add(simPool);
        }
        persist2.setSimPoolSet(temp);
        persit2ToRedis(persist2);
    }

    private void persit2ToRedis(Persist2 persist2){
        rloClient.persist(persist2);
    }

    /**
     * 单个RLO对象也需要手动关联
     */
    @RequestMapping("/persist2_simtest3")
    public void persit2SimTest3(){
        SimTest3 simTest3 = new SimTest3();
        simTest3.setName("simTest3");
        SimGroup simGroup = new SimGroup();
        simGroup.setName("simGroup");
        simTest3.setSimGroup(simGroup);

        rloClient.persist(simGroup);
        SimTest3 temp = rloClient.persist(simTest3);
        log.info("simTest3:{}", temp);
        simGroup = rloClient.get(SimGroup.class, simGroup.getName());
        temp.setSimGroup(simGroup);
        log.info("simTest3:{}", temp);
    }


    /**
     * 此关联会被持久化，杀进程也会保留
     */
    @RequestMapping("/persist2_attach")
    public void attach(){
        Persist2 persist2 = rloClient.get(Persist2.class,redisKey);
        persist2.setSimPoolSet(innerSet);
    }

    @RequestMapping("/persist2_print")
    public void print(){
        Persist2 persist2 = rloClient.get(Persist2.class,redisKey);
        log.info(persist2.getName());
        if( persist2.getSimPoolSet() != null && !persist2.getSimPoolSet().isEmpty() ){
            for (SimPool simPool : persist2.getSimPoolSet()){
                log.info("simPool:{}", simPool==null?"":simPool.getName());
            }

        }else{}
    }
}
