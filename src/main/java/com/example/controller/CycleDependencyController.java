package com.example.controller;

import com.example.entity.Cycle1;
import com.example.entity.Cycle2;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;

/**
 * @author alexouyang
 * @Date 2019-12-03
 */
@RestController
@Slf4j
public class CycleDependencyController {

    RedissonClient redissonClient;

    RLiveObjectService rloClient;

    public CycleDependencyController( RedissonClient redissonClient ){
        this.redissonClient = redissonClient;
        rloClient = redissonClient.getLiveObjectService();
    }

    /**
     * 演示循环依赖,不会报错
     * toString做了特殊处理，否则会报StackOverflow
     */
    @RequestMapping("/cycle")
    public void cycle(){
        Cycle1 cycle1 = new Cycle1();
        Cycle2 cycle2 = new Cycle2();
        cycle1.setName("cycle1");
        cycle2.setName("cycle2");
        cycle1 = rloClient.persist(cycle1);
        cycle2 = rloClient.persist(cycle2);

        cycle1.setCycle2Set(new HashSet<>());
        cycle1.getCycle2Set().add(cycle2);

        cycle2.setCycle1(cycle1);

        cycle1.toString();
    }
}
