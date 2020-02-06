package com.example.controller;

import com.example.entity.AdvRLO0;
import com.example.entity.AdvRLO1;
import com.example.entity.AdvRLO2;
import com.example.entity.AdvRLO3;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author alexouyang
 * @Date 2020-01-20
 */
@RestController
@Slf4j
public class AdvRLOController {

    private RLiveObjectService rloClient;

    @Autowired
    AdvRLOController(RedissonClient redissonClient){
        this.rloClient = redissonClient.getLiveObjectService();
    }

    @RequestMapping("/graph_persist")
    public void test1(){
        AdvRLO0 advRLO0 = new AdvRLO0();
        advRLO0.setName("advRLO0");
        AdvRLO1 advRLO1 = new AdvRLO1();
        advRLO1.setName("advRLO1");
        AdvRLO2 advRLO2 = new AdvRLO2();
        advRLO2.setName("advRLO2");
        AdvRLO3 advRLO3 = new AdvRLO3();
        advRLO3.setName("testAdvRLO3");
        AdvRLO3 innerRLO3 = new AdvRLO3();
        innerRLO3.setName("innerRLO3");

        /**
         * AdvRLO0 refer AdvRLO1
         * AdvRLO0 refer AdvRLO2
         * list test case
         */
        List<AdvRLO1> list = new ArrayList<>();
        list.add(advRLO1);
        advRLO0.setTestAdvRLO1List(list);
        advRLO0.setTestAdvRLO2Object(advRLO2);

        /**
         * AdvRLO1 refer AdvRLO2
         * AdvRLO0 refer AdvRLO1 refer AdvRLO2 cycle reference
         * Map testcase
         */
        Map<String, AdvRLO2> map = new HashMap<>();
        map.put("mapkey1", advRLO2);
        advRLO1.setAdvRLO2Map(map);

        /**
         * AdvRLO2 refer AdvRLO0
         * AdvRLO2 refer AdvRLO3
         * AdvRLO2 refer AdvRLO0 cycle reference
         */
        advRLO2.setAdvRLO0(advRLO0);
        advRLO2.setAdvRLO3(advRLO3);

        /**
         * AdvRLO3 --- AdvRLO3 self cycle reference
         * direct self RLO reference
         */
        advRLO3.setTestAdvRLO3(innerRLO3);
        innerRLO3.setTestAdvRLO3(advRLO3);

        rloClient.registerClass(AdvRLO0.class);
        rloClient.registerClass(AdvRLO1.class);
        rloClient.registerClass(AdvRLO2.class);
        rloClient.registerClass(AdvRLO3.class);
        AdvRLO0 temp0 = rloClient.persistVertex(advRLO0);
        AdvRLO1 temp1 = rloClient.persistVertex(advRLO1);
        AdvRLO2 temp2 = rloClient.persistVertex(advRLO2);
        AdvRLO3 temp3 = rloClient.persistVertex(advRLO3);

        rloClient.persistEdge(advRLO0, temp0);
        rloClient.persistEdge(advRLO1, temp1);
        rloClient.persistEdge(advRLO2, temp2);
        rloClient.persistEdge(advRLO3, temp3);

        List<AdvRLO1> resultList = temp2.getAdvRLO0().getTestAdvRLO1List();
        for(AdvRLO1 element : resultList){
            log.info("inner list testcase:{}", element== null ? null : element.getName());
            Map<String, AdvRLO2> resultMap = element.getAdvRLO2Map();
            Iterator<Entry<String, AdvRLO2>> itr = resultMap.entrySet().iterator();
            while( itr.hasNext() ){
                Entry<String, AdvRLO2> entry = itr.next();
                log.info("map testcase, key={}, value={}", entry.getKey(), entry.getValue());
            }
        }
        log.debug("success");

    }

    /**
     * Will fail into StackOverFlow Exception
     */
    @RequestMapping("/common_persist")
    public void test2(){
        AdvRLO0 advRLO0 = new AdvRLO0();
        advRLO0.setName("commonRLO0");
        AdvRLO1 advRLO1 = new AdvRLO1();
        advRLO1.setName("commonRLO1");
        AdvRLO2 advRLO2 = new AdvRLO2();
        advRLO2.setName("commonRLO2");
        AdvRLO3 advRLO3 = new AdvRLO3();
        advRLO3.setName("commonRLO3");

        List<AdvRLO1> list = new ArrayList<>();
        list.add(advRLO1);
        advRLO0.setTestAdvRLO1List(list);
        advRLO0.setTestAdvRLO2Object(advRLO2);

        // Cycle
        advRLO2.setAdvRLO0(advRLO0);

        AdvRLO0 temp0 = rloClient.persist(advRLO0);
        AdvRLO1 temp1 = rloClient.persist(advRLO1);
        AdvRLO2 temp2 = rloClient.persist(advRLO2);
        AdvRLO3 temp3 = rloClient.persist(advRLO3);
        log.info("adv0:{}", temp0);
        List<AdvRLO1> resultList = temp0.getTestAdvRLO1List();
        for(AdvRLO1 element : resultList){
            log.info("debug{}", element== null ? null : element.getName());
        }
    }
}
