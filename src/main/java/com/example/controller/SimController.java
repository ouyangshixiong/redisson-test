package com.example.controller;

import com.example.entity.*;
import com.example.service.RedissonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author alexouyang
 * @Date 2019-10-12
 */
@RestController
@Slf4j
public class SimController {

    @Autowired
    RedissonService redissonService;

    @RequestMapping("/sim1")
    public void sim1(){
        redissonService.registerRLO(Sim.class);
        redissonService.registerRLO(SimGroup.class);
        redissonService.registerRLO(SimGroupInPool.class);
        redissonService.registerRLO(SimPool.class);


        Sim simInRedis = (Sim)redissonService.getRLO("sim1", Sim.class);
        if( simInRedis == null ){
            log.info("sim1 is null");
            Sim sim = new Sim();
            sim.setName("sim1");

            List<SimGroup> simGroupList = new ArrayList<>();
            for (int i = 0; i <2 ; i++) {
                simGroupList.add(constructorSimGroup("simGroup" + i ));
            }
            sim.setSimGroupList(simGroupList);
            redissonService.persistRLO(sim);
            log.info("persist completed!");
        }else{
            log.info("test case");
            increasePrimary();
            increaseByMethod();
        }
    }

    /**
     *  这个测试用例会失败
     */
    private void increasePrimary(){
        SimPool simPool = (SimPool)redissonService.getRLO("simPool0", SimPool.class);
        log.info("increasePrimary before increase, count=" + simPool.getCount());
        simPool.increasePrimary();
        log.info("increasePrimary after increase, count=" + simPool.getCount() + " failed to increase!");

    }

    /**
     * 这个测试用例会成功
     */
    private void increaseByMethod(){
        SimPool simPool = (SimPool)redissonService.getRLO("simPool0", SimPool.class);
        log.info("increaseByMethod before increase, count=" + simPool.getCount());
        simPool.increaseByMethod();
        log.info("increaseByMethod after increase, count=" + simPool.getCount());
    }

    private SimGroup constructorSimGroup( String persistName ){
        SimGroup simGroup = new SimGroup();
        simGroup.setName(persistName);
        log.info("persist SimGroup:" + persistName);
        //SimPoolSet
        Set<SimPool> simPoolSet = new HashSet<>();
        for (int i = 0; i <3 ; i++) {
            simPoolSet.add(constructorSimPool("simPool" + i));
        }
        simGroup.setSimPoolSet(simPoolSet);

        //SimGroupInPoolSet
        Set<SimGroupInPool> simGroupInPoolSet = new HashSet<>();
        for (int i = 0; i <4 ; i++) {
            simGroupInPoolSet.add(constructorSimGroupInPool("simGroupInPool" + i));
        }
        simGroup.setSimGroupInPoolSet(simGroupInPoolSet);

        return simGroup;
    }

    private SimGroupInPool constructorSimGroupInPool( String persistName ){
        SimGroupInPool simGroupInPool = new SimGroupInPool();
        simGroupInPool.setName(persistName);
        return simGroupInPool;
    }

    private SimPool constructorSimPool( String persistName ){
        SimPool simPool = new SimPool();
        simPool.setName(persistName);
        return simPool;
    }

}
