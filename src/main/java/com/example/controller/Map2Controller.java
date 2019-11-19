package com.example.controller;

import com.example.entity.SimGroup;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

/**
 * @author alexouyang
 * @Date 2019-11-19
 */
@RestController
@Slf4j
public class Map2Controller {

    RedissonClient redissonClient;

    RLiveObjectService rloClient;

    public Map2Controller( RedissonClient redissonClient ){
        this.redissonClient = redissonClient;
        rloClient = redissonClient.getLiveObjectService();
    }

    /**
     * set 没有持久化，redis只有101个对象
     */
    @RequestMapping("/map2")
    public void map2(){
        RMap<String, Set<SimGroup>> map2 = redissonClient.getMap("map2");
        for (int j = 0; j <10; j++) {
            Set<SimGroup> aSet = new HashSet<>();
            for (int i = 0; i <10 ; i++) {
                SimGroup simGroup = new SimGroup();
                simGroup.setName("simGroup" + i+(j*100));
                rloClient.persist(simGroup);
                aSet.add(simGroup);
            }
            //put
            map2.put("key" + j, aSet);
        }
    }

    @RequestMapping("/map3")
    public void map3(){
        RMap<String, Set<SimGroup>> map3 = redissonClient.getMap("map3");
        for (int j = 0; j <10; j++) {
            RSet<SimGroup> rSet = redissonClient.getSet("rset" + j);
            for (int i = 0; i <10 ; i++) {
                SimGroup simGroup = new SimGroup();
                simGroup.setName("simGroup" + i+(j*1000));
                simGroup = rloClient.persist(simGroup);
                rSet.add(simGroup);
            }
            //put
            map3.put("key" + j, rSet);
        }
    }

}
