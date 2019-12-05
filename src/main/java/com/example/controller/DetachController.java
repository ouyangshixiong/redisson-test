package com.example.controller;

import com.example.entity.Cycle2;
import com.example.entity.DetachObject1;
import jodd.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author alexouyang
 * @Date 2019-12-04
 */
@RestController
@Slf4j
public class DetachController {


    RedissonClient redissonClient;

    RLiveObjectService rloClient;

    private ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("my-worker-%d").get();

    private Executor executor = new ThreadPoolExecutor(5,20,2, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), threadFactory);

    public DetachController( RedissonClient redissonClient ){
        this.redissonClient = redissonClient;
        rloClient = redissonClient.getLiveObjectService();
    }


    @RequestMapping("/detach_setup")
    public void detachObjectSetup(){
        //init
        long ct1 = System.currentTimeMillis();
        DetachObject1 testObj = new DetachObject1();
        testObj.setName("detach_object1_0");
        testObj = rloClient.persist(testObj);
        for (int i = 0; i < 1000; i++) {
            Cycle2 temp = new Cycle2();
            temp.setName("cycle2_" + i);
            temp = rloClient.persist(temp);
            if( testObj.getCycle2List() == null ){
                testObj.setCycle2List(new ArrayList<>());
            }else{}
            testObj.getCycle2List().add(temp);
        }
        log.info("init time cost {}ms", System.currentTimeMillis()-ct1);
        //end init
    }

    @RequestMapping("/detach_calc")
    public void detachCalc(){
                // attach status operation
                String persistId = "detach_object1_0";
                long ct2 = System.currentTimeMillis();
                DetachObject1 attached = rloClient.get(DetachObject1.class, persistId);
                RLock lock = redissonClient.getLock(persistId + "_count");
                try{
                    lock.tryLock(1,10,TimeUnit.SECONDS);
                    // logic
                    attached.increaseByMethod();
                }catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                }finally {
                    if( lock.isLocked() ){
                        lock.unlock();
                    }else{}
                }

                for ( Cycle2 cycle2 : attached.getCycle2List()){
                    RLock lock2 = redissonClient.getLock(persistId + "_" + cycle2.getName() + "_count");
                    try{
                        lock2.tryLock(1,10,TimeUnit.SECONDS);
                        // logic
                        cycle2.increaseByMethod();
                    }catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                    }finally {
                        if( lock2.isLocked() ){
                            lock2.unlock();
                        }else{}
                    }
                }

                log.info("attached status +1 operation {}ms", System.currentTimeMillis()-ct2);
                // end attach status operation
    }

    @RequestMapping("/detach_calc2")
    public void detachCalc2(){
        String persistId = "detach_object1_0";
        long ct2 = System.currentTimeMillis();
        DetachObject1 attached = rloClient.get(DetachObject1.class, persistId);
        DetachObject1 detached = rloClient.detach(attached);
        // logic
        detached.increaseByMethod();
        List<Cycle2> list = detached.getCycle2List();
        for (Cycle2 cycle2 : list){
            cycle2 = rloClient.detach(cycle2);
            cycle2.increaseByMethod();
        }
        log.info("detach status +1 operation {}ms", System.currentTimeMillis()-ct2);
    }

    @RequestMapping("/detach_print")
    public void printResult(){
        String persistId = "detach_object1_0";
        DetachObject1 attached = rloClient.get(DetachObject1.class, persistId);
        log.info("DetachObject1{}, count {}", attached.getName(),attached.getCount());
        for ( Cycle2 cycle2 : attached.getCycle2List()){
            log.info("innerList Cycle2:{}, count {}", cycle2.getName(), cycle2.getCount());
        }
    }
}
