package com.example.controller;

import com.example.entity.*;
import jodd.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author alexouyang
 * @Date 2019-12-10
 */
@RestController
@Slf4j
public class PersistController {
    private final String redisKey = "detach_object1_2";

    RedissonClient redissonClient;

    RLiveObjectService rloClient;

//    Set<SimPool> simPoolSet = new HashSet<>();
//    Set<SimPool> simPoolSet = new ConcurrentSkipListSet<>();


    public PersistController( RedissonClient redissonClient ){
        this.redissonClient = redissonClient;
        rloClient = redissonClient.getLiveObjectService();
//        init();
    }

//    private void init(){
//        for (int i = 0; i <10 ; i++) {
//            SimPool simPool = new SimPool();
//            simPool.setName("simPool_" + i);
//            simPool.setId(i);
//            simPoolSet.add(simPool);
//        }
//    }

    @RequestMapping("/persist_myobj")
    public void hashSetTest(){
        JavaTypeObject javaTypeObject = new JavaTypeObject();
        javaTypeObject.setName("myobj");
        javaTypeObject.setSimPoolSet(new HashSet<>());
        for (int i = 0; i <10 ; i++) {
            SimPool simPool = new SimPool();
            simPool.setName("simPool_" + i);
            simPool.setId(i);
            javaTypeObject.getSimPoolSet().add(simPool);
        }
        rloClient.merge(javaTypeObject);

    }

    @RequestMapping("/persist_myobj2")
    public void hashSetTest2(){
        JavaTypeObject2 javaTypeObject2 = new JavaTypeObject2();
        javaTypeObject2.setName("myobj2");
        javaTypeObject2.setMyObjectSet(new HashSet<>());
        for (int i = 0; i <10 ; i++) {
            MyObject myObject = new MyObject();
            myObject.setName(""+i);
            myObject.setId(i);
            javaTypeObject2.getMyObjectSet().add(myObject);
        }
        rloClient.persist(javaTypeObject2);

    }


}
