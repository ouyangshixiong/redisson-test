package com.example.controller;

import com.example.entity.Sim;
import com.example.entity.SimGroup;
import com.example.entity.SimGroupInPool;
import com.example.entity.SimPool;
import com.example.service.RedissonService;
import jodd.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 多线程下无法保证一致性
 * @author alexouyang
 * @Date 2020-01-06
 */
@RestController
@Slf4j
public class TransactionController {
    @Autowired
    RedissonService rloClient;

    private ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("my-worker-%d").get();

    private Executor executor = new ThreadPoolExecutor(5,20,2, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), threadFactory);

    private Sim sim;

    @RequestMapping("/transaction_setup")
    public void batchSim(){
        // init counterMap
        RMap<Long,Long> counterMap = rloClient.getMap("counterMap");
        sim = new Sim(counterMap);
        log.warn("counterId{}", sim.getCounterId());
        sim.setName("sim1");

        List<SimGroup> simGroupList = new CopyOnWriteArrayList<>();
        sim.setSimGroupList(simGroupList);

        SimGroup simGroup = new SimGroup(counterMap);
        simGroup.setName("simGroup1");
//        simGroupList.add(simGroup);

        Set<SimPool> simPoolSet = new HashSet<>();

        simGroup.setSimPoolSet(simPoolSet);

        SimPool simPool = new SimPool(counterMap);
        simPool.setName("simPool1");
        simPool.setId(1);

        Set<SimGroupInPool> simGroupInPoolSet = new HashSet<>();

        simGroup.setSimGroupInPoolSet(simGroupInPoolSet);

        SimGroupInPool simGroupInPool = new SimGroupInPool(counterMap);
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


    @RequestMapping("/transaction_add")
    public void bathAdd(){
        RTransaction transaction = rloClient.getTransaction();
        for (int i = 0; i <100 ; i++) {
            executor.execute(()->{
                if( sim == null ){
                    sim = (Sim)rloClient.getRLO("sim1", Sim.class);
                }else{}
                if( sim == null ){
                    log.info("sim is null");
                }else{
                    long counter1 = (long)rloClient.getMap("counterMap").get(sim.getCounterId());
                    sim.increaseByTransaction(transaction);
                    long counter2 = (long)rloClient.getMap("counterMap").get(sim.getCounterId());
                    log.warn("sim counter1={}, counter2={}", counter1, counter2);
                    for (SimGroup simGroup : sim.getSimGroupList()){
                        simGroup.increaseByTransaction(transaction);

                        for( SimPool simPool : simGroup.getSimPoolSet() ){
                            simPool.increaseByTransaction(transaction);
                        }

                        for( SimGroupInPool simGroupInPool : simGroup.getSimGroupInPoolSet()){
                            simGroupInPool.increaseByTransaction(transaction);
                        }
                    }
                }
                transaction.commit();
            });
        }
    }

    @RequestMapping("/transaction_print")
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
