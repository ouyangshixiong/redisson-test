package com.example.controller;

import com.example.entity.CommonObject;
import com.example.entity.RLOCollectionWrapper;
import com.example.entity.SimGroup;
import com.example.persistance.AbstractEntity;
import com.example.persistance.RedissonRLOWrapper;
import com.example.persistance.Simo1;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
    RedissonRLOWrapper redissonRLOWrapper;

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

    @RequestMapping("/test23")
    public void test23()throws IOException {
        long bt = System.currentTimeMillis();
        List<Simo1> simo1List = new ArrayList<>();
        for (int i = 0; i <100 ; i++) {
            Simo1 simo1 = new Simo1();
            simo1.setPersistName("simo" + i);
            simo1List.add(simo1);
            if( i % 10 == 0 ){
                log.info("test23 process number:{}", i );
            }
        }
        redissonRLOWrapper.init(simo1List, Simo1.class);
        log.info("test23 {}ms", System.currentTimeMillis()-bt );
        redissonRLOWrapper.delete(  (AbstractEntity) redissonRLOWrapper.getDataMapWrapper().get("simo5"), Simo1.class);
        List<Simo1> list = (List<Simo1>)redissonRLOWrapper.getDataListWrapper();
    }

    /**
     * fail test
     * @throws IOException
     */
    @RequestMapping("/test24")
    public void test24()throws IOException {
        long bt = System.currentTimeMillis();
        List<CommonObject> commonObjectList = new ArrayList<>();
        for (int i = 0; i <100 ; i++) {
            CommonObject commonObject = new CommonObject();
            commonObjectList.add(commonObject);
            if( i % 10 == 0 ){
                log.info("test24 process number:{}", i );
            }
        }
        redissonRLOWrapper.init(commonObjectList, CommonObject.class);
        log.info("test24 {}ms", System.currentTimeMillis()-bt );
    }
}
