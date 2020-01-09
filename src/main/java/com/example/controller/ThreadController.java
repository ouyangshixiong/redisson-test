package com.example.controller;

import com.example.entity.SimPool;
import com.example.service.RedissonService;
import jodd.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;

/**
 * 演示Redisson正确的加锁解锁方法
 * @author alexouyang
 * @Date 2019-11-04
 */
@RestController
@Slf4j
public class ThreadController {
    @Autowired
    RedissonService<SimPool> redissonService;

    private ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("my-worker-%d").get();

    private Executor executor = new ThreadPoolExecutor(5,20,2, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), threadFactory);

    @RequestMapping("/threads")
    public void threads(){
        for (int i = 0; i <100 ; i++) {
            executor.execute(()-> {
                    String key = "simPool0";
                    RLock lock = redissonService.getReadWriteLock(key).writeLock();
                    log.info(Thread.currentThread().getName() + " try to lock on key=" + key);
                    try {
                        lock.tryLock(10, 1, TimeUnit.SECONDS);
                        if (!Thread.currentThread().isInterrupted()) {
                            SimPool simPool = redissonService.getRLO(key, SimPool.class);
                            if (simPool == null) {
                                simPool = new SimPool();
                                simPool.setName(key);
                                simPool = redissonService.persistRLO(simPool);
                            } else {
                            }
                            simPool.increaseByMethod();
                            log.info("After increaseByBatch, count=" + simPool.queryCount());
                        } else {
                            log.error(Thread.currentThread().getName() + "failed to lock");
                        }
                    }catch( InterruptedException e ){
                        Thread.currentThread().interrupt();
                    }finally {
                        if( lock.isLocked() ){
                            lock.unlock();
                        }else{}
                    }
            });
        }
    }


    @RequestMapping("/nolock")
    public void nolock(){
        for (int i = 0; i <100 ; i++) {
            executor.execute(()-> {
                String key = "simPool1";
                try{
                    SimPool simPool = redissonService.getRLO(key, SimPool.class);
                    if( simPool == null ){
                        simPool = new SimPool();
                        simPool.setName(key);
                        simPool = redissonService.persistRLO(simPool);
                    }else{}
                    simPool.increaseByMethod();
                    log.info("After increaseByBatch, count=" + simPool.queryCount());
                }catch(Throwable e){
                    log.error("unexpected Exception/Error ", e);
                }
            });
        }
    }
}
