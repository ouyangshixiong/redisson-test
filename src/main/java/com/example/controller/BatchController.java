package com.example.controller;

import com.example.entity.Sim;
import com.example.entity.SimGroup;
import com.example.entity.SimGroupInPool;
import com.example.entity.SimPool;
import com.example.service.RedissonService;
import jodd.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.BatchResult;
import org.redisson.api.RBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.*;

/**
 * 不可行，只可保证最终一致性
 * @author alexouyang
 * @Date 2020-01-02
 */
@RestController
@Slf4j
public class BatchController {

    @Autowired
    RedissonService rloClient;

    private ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("my-worker-%d").get();

    private Executor executor = new ThreadPoolExecutor(5,20,2, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), threadFactory);

    private Sim sim;

    @RequestMapping("/batch_sim_setup")
    public void batchSim(){
        SnowFlake snowFlake = new SnowFlake(2,3);
        sim = new Sim(snowFlake.nextId());
        sim.setName("sim1");

        List<SimGroup> simGroupList = new CopyOnWriteArrayList<>();
        sim.setSimGroupList(simGroupList);

        SimGroup simGroup = new SimGroup(snowFlake.nextId());
        simGroup.setName("simGroup1");
//        simGroupList.add(simGroup);

        Set<SimPool> simPoolSet = new HashSet<>();

        simGroup.setSimPoolSet(simPoolSet);

        SimPool simPool = new SimPool(snowFlake.nextId());
        simPool.setName("simPool1");
        simPool.setId(1);

        Set<SimGroupInPool> simGroupInPoolSet = new HashSet<>();

        simGroup.setSimGroupInPoolSet(simGroupInPoolSet);

        SimGroupInPool simGroupInPool = new SimGroupInPool(snowFlake.nextId());
        simGroupInPool.setName("simGroupInPool1");
        simGroupInPool.setId(1);

        // persist
        simGroupInPool = (SimGroupInPool)rloClient.persistRLO(simGroupInPool);
        simPool = (SimPool)rloClient.persistRLO(simPool);
        simGroup = (SimGroup)rloClient.persistRLO(simGroup);
        sim = (Sim)rloClient.persistRLO(sim);

        // attach 这样可以
//        sim.getSimGroupList().add(simGroup);
//        SortedSet<SimGroupInPool> temp = new ConcurrentSkipListSet<>();
//        temp.add(simGroupInPool);
//        simGroup.setSimGroupInPoolSet(temp);
//        SortedSet<SimPool> temp2 = new ConcurrentSkipListSet<>();
//        temp2.add(simPool);
//        simGroup.setSimPoolSet(temp2);

        // attach 这样也可以
        sim.getSimGroupList().add(simGroup);
        simGroup.getSimGroupInPoolSet().add(simGroupInPool);
        simGroup.getSimPoolSet().add(simPool);
    }

    @RequestMapping("/batch_add")
    public void bathAdd(){
        for (int i = 0; i <20 ; i++) {
            executor.execute(()->{
                RBatch batch = rloClient.getBatch();
                if( sim == null ){
                    sim = (Sim)rloClient.getRLO("sim1", Sim.class);
                }else{}
                if( sim == null ){
                    log.info("sim is null");
                }else{
                    sim.increaseByBatch(batch);
                    for (SimGroup simGroup : sim.getSimGroupList()){
                        simGroup.increaseByBatch(batch);

                        for( SimPool simPool : simGroup.getSimPoolSet() ){
                            simPool.increaseByBatch(batch);
                        }

                        for( SimGroupInPool simGroupInPool : simGroup.getSimGroupInPoolSet()){
                            simGroupInPool.increaseByBatch(batch);
                        }
                    }
                }
                batch.execute();
            });
        }
    }

    @RequestMapping("/batch_sim_print")
    public void printSim(){
        if( sim == null ){
            sim = (Sim)rloClient.getRLO("sim1", Sim.class);
        }else{}
        if( sim == null ){
            log.info("sim is null");
        }else{
            log.info(sim.toString());
        }
    }
}
