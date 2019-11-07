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
 * @author alexouyang
 * @Date 2019-11-04
 */
@RestController
@Slf4j
public class ThreadController {
    @Autowired
    RedissonService redissonService;

    @RequestMapping("/threads")
    public void threads(){

        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("my-worker-%d").get();
        Executor executor = new ThreadPoolExecutor(5,20,2, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), threadFactory);
        for (int i = 0; i <100 ; i++) {
            executor.execute(()-> {
                    String key = "simPool0";
                    RLock lock = redissonService.getReadWriteLock(key).writeLock();
                    log.info(Thread.currentThread().getName() + " try to lock on key=" + key);
                    lock.lock(1, TimeUnit.SECONDS);
                    if( !Thread.currentThread().isInterrupted() ){
                        try{
                            SimPool simPool = (SimPool)redissonService.getRLO(key, SimPool.class);
                            simPool.increaseByMethod();
                            log.info("After increase, count=" + simPool.getCount());
                            lock.unlock();
                        }catch(Throwable e){
                            log.error("unexpected Exception/Error ", e);
                        }
                    }else{
                        log.error( Thread.currentThread().getName() +"failed to lock");
                    }

            });
        }
    }
}