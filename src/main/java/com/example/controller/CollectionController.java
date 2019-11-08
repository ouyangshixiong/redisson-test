package com.example.controller;

import com.example.entity.RLOCollectionWrapper;
import com.example.entity.SimGroup;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alexouyang
 * @Date 2019-11-07
 */
@RestController
@Slf4j
public class CollectionController {

    RedissonClient redissonClient;

    RLiveObjectService rloClient;

    @Autowired
    CollectionController(RedissonClient redissonClient){
        this.redissonClient = redissonClient;
        rloClient = redissonClient.getLiveObjectService();
    }

    @RequestMapping("/collection")
    public void collection(){
        RLOCollectionWrapper<SimGroup> wrapper = new RLOCollectionWrapper<>();
        List<SimGroup> list = new ArrayList<>();
        for (int i = 0; i <30 ; i++) {
            SimGroup simGroup = new SimGroup();
            simGroup.setName("simPool" + i);
            list.add(simGroup);
        }
        wrapper.putByPage(rloClient, "simPoolWrapper1", list);
    }

    @RequestMapping("/test22")
    public void test2(){
        List<SimGroup> list = redissonClient.getList("simPoolList");
        long bt = System.currentTimeMillis();
        for (int i = 0; i <10000 ; i++) {
            SimGroup simGroup = new SimGroup();
            simGroup.setName("simPool" + i);
            simGroup = rloClient.persist(simGroup);
            list.add(simGroup);
            if( i % 1000 == 0 ){
                log.info("test22 process number:{}", i );
            }
        }
        log.info("test22 {}", System.currentTimeMillis()-bt );
    }
}
