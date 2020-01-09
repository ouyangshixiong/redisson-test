package com.example.controller;

import com.example.entity.*;
import com.example.service.RedissonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author alexouyang
 * @Date 2019-10-12
 */
@RestController
@Slf4j
public class SimController {

    @Autowired
    RedissonService redissonService;

    @RequestMapping("/bigMap")
    public void bigMap(){
        redissonService.registerRLO(SimPoolMapWrapper.class);
        SimPoolMapWrapper bigMap = (SimPoolMapWrapper)redissonService.getRLO("bigmap", SimPoolMapWrapper.class);
        if( bigMap == null ){
            SimPoolMapWrapper mapWrapper = new SimPoolMapWrapper();
            mapWrapper.setName("bigmap");
            Map<String, SimPool> tempMap = new HashMap<>();
            for (int i = 0; i <10000 ; i++) {
                //重复的key，看看有没有问题
                String persistName = "simPool" + i;
                tempMap.put("simPool" + i, constructorSimPool(persistName));
            }
            mapWrapper.setSimPoolMap(tempMap);
            long bt = System.currentTimeMillis();
            redissonService.persistRLO(mapWrapper);
            log.info("persist big RLO map time cost:" + (System.currentTimeMillis()-bt) + "ms.");
        }else{
            log.info(bigMap.getName());
            long bt = System.currentTimeMillis();
            Map<String,SimPool> data = bigMap.getSimPoolMap();
            log.info("get big RLO map time cost:" + (System.currentTimeMillis()-bt) + "ms.");
            for ( String key :  data.keySet()) {
//                log.info(data.get(key).getName());
            }
        }

    }

    @RequestMapping("/bigMapPage")
    public void bigMapPage(){
        redissonService.registerRLO(SimPoolMapWrapper.class);
        SimPoolMapWrapper bigMap = (SimPoolMapWrapper)redissonService.getRLO("bigmap", SimPoolMapWrapper.class);
        if( bigMap == null ){
            long bt = System.currentTimeMillis();
            SimPoolMapWrapper mapWrapper = new SimPoolMapWrapper();
            mapWrapper.setName("bigmap");
            Map<String, SimPool> tempMap = new HashMap<>();
            for (int i = 0; i <1000 ; i++) {
                //重复的key，看看有没有问题
                String persistName = "simPool" + i;
                tempMap.put("simPool" + i, constructorSimPool(persistName));
            }
            mapWrapper.setSimPoolMap(tempMap);
            redissonService.persistRLO(mapWrapper);
            log.info("1 page persist RLO time cost:" + (System.currentTimeMillis()-bt) + "ms.");
        }else{
            for (int j = 1; j <10 ; j++) {
                long bt = System.currentTimeMillis();
                SimPoolMapWrapper mapWrapper = new SimPoolMapWrapper();
                mapWrapper.setName("bigmap_part" + j);
                Map<String, SimPool> tempMap = new HashMap<>();
                for (int i = 0; i <1000 ; i++) {
                    //重复的key，看看有没有问题
                    String persistName = "simPool" + j*1000 + i;
                    tempMap.put("simPool" + i, constructorSimPool(persistName));
                }
                mapWrapper.setSimPoolMap(tempMap);
                mapWrapper = (SimPoolMapWrapper)redissonService.persistRLO(mapWrapper);
                bigMap.getSimPoolMap().putAll(mapWrapper.getSimPoolMap());
                //TODO 删除临时map
                log.info( (j+1) +" page persist RLO time cost:" + (System.currentTimeMillis()-bt) + "ms.");
            }
        }

    }

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
        log.info("increasePrimary before increaseByBatch, count=" + simPool.queryCount());
//        simPool.increasePrimary();
        log.info("increasePrimary after increaseByBatch, count=" + simPool.queryCount() + " failed to increaseByBatch!");

    }

    /**
     * 这个测试用例会成功
     */
    private void increaseByMethod(){
        SimPool simPool = (SimPool)redissonService.getRLO("simPool0", SimPool.class);
        log.info("increaseByMethod before increaseByBatch, count=" + simPool.queryCount());
        simPool.increaseByMethod();
        log.info("increaseByMethod after increaseByBatch, count=" + simPool.queryCount());
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
