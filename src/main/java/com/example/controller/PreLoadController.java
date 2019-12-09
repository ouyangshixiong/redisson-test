package com.example.controller;

import com.example.entity.Cycle2;
import com.example.entity.DetachObject1;
import jodd.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author alexouyang
 * @Date 2019-12-09
 */
@RestController
@Slf4j
public class PreLoadController {
    private final String redisKey = "preload_object1_1";

    private DetachObject1 testObj;

    RedissonClient redissonClient;

    RLiveObjectService rloClient;

    private ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("my-worker-%d").get();

    private Executor executor = new ThreadPoolExecutor(5,20,2, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), threadFactory);

    public PreLoadController( RedissonClient redissonClient ){
        this.redissonClient = redissonClient;
        rloClient = redissonClient.getLiveObjectService();
    }

    /**
     * 清理redis
     */
    @RequestMapping("/preload_clean")
    public void cleanUpRedis(){
        testObj = rloClient.get(DetachObject1.class, redisKey);
        if( testObj != null ){
            rloClient.delete(testObj);
        }else{}
    }

    /**
     * setUp
     */
    @RequestMapping("/preload_setup")
    public void setup(){
        //init
        long ct1 = System.currentTimeMillis();
        testObj = new DetachObject1();
        testObj.setName(redisKey);
        for (int i = 0; i < 1000; i++) {
            Cycle2 temp = new Cycle2();
            temp.setName("cycle2_" + i);
            if( testObj.getCycle2List() == null ){
                testObj.setCycle2List(new ArrayList<>());
            }else{}
            testObj.getCycle2List().add(temp);
        }
        log.info("setup time cost {}ms", System.currentTimeMillis()-ct1);
        //end init
    }

    private void doCalc(){
        long ct2 = System.currentTimeMillis();
        // logic
        testObj.increaseByMethod();
        List<Cycle2> list = testObj.getCycle2List();
        for (Cycle2 cycle2 : list){
            cycle2.increaseByMethod();
        }
        log.info("doCalc status +1 operation {}ms", System.currentTimeMillis()-ct2);
    }

    /**
     * 使用多线程并发+1
     */
    @RequestMapping("/preload_batch")
    public void batchCalc2(){
        for (int i = 0; i < 10; i++) {
            executor.execute(()->{
                //因为本例只有一个对象，所以就简化了对单个对象的sync
                synchronized (testObj){
                    doCalc();
                }
            });
        }
    }

    @RequestMapping("/preload_persist")
    public void persist(){
        rloClient.persist(testObj);
    }

    @RequestMapping("/preload_print_local")
    public void printLocalResult(){
        if( testObj == null ){
            return;
        }else{}
        log.info("local attached{}, count {} innerList.size {}", testObj.getName(),testObj.getCount(), testObj.getCycle2List().size());
        List<Cycle2> list = testObj.getCycle2List();
        for (int i = 0; i < list.size() ; i++) {
            if( i % 100 == 0 ){
                log.info("local innerList :{}, count {}", list.get(i).getName(), list.get(i).getCount());
            }else{}
        }
    }

    @RequestMapping("/preload_print_redis")
    public void printRedisResult(){
        DetachObject1 temp = rloClient.get(DetachObject1.class, redisKey);
        log.info("redis attached{}, count {} innerList.size {}", temp.getName(),temp.getCount(), temp.getCycle2List().size());
        List<Cycle2> list = temp.getCycle2List();
        for (int i = 0; i < list.size() ; i++) {
            if( i % 100 == 0 ){
                log.info("redis innerList :{}, count {}", list.get(i).getName(), list.get(i).getCount());
            }else{}
        }
    }
}
