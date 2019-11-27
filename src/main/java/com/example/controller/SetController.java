package com.example.controller;

import com.example.entity.SimTest;
import com.example.entity.SimTest2;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
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
public class SetController {

    RedissonClient redissonClient;

    RLiveObjectService rloClient;

    public SetController( RedissonClient redissonClient ){
        this.redissonClient = redissonClient;
        rloClient = redissonClient.getLiveObjectService();
    }

    /**
     * SimTest类中标注了@RCascade(RCascadeType.ALL)
     * 持久化了2个对象
     */
    @RequestMapping("/simTest")
    public void simTest(){
        SimTest simTest = new SimTest();
        simTest.setName("simTest");
        Set<String> innerSet = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            innerSet.add("bbb" + i);
        }
        simTest.setInnerSet(innerSet);
        simTest = rloClient.persist(simTest);
        for (String element : simTest.getInnerSet()){
            log.info("simTest {}", element);
        }
    }

    /**
     * 迟初始化
     *
     */
    @RequestMapping("/simTestLate")
    public void simTestLate(){
        SimTest simTest = new SimTest();
        simTest.setName("simTest");
        simTest = rloClient.persist(simTest);
        Set<String> innerSet = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            innerSet.add("bbb" + i);
        }
        simTest.setInnerSet(innerSet);
        for (String element : simTest.getInnerSet()){
            log.info("simTest {}", element);
        }
    }

    /**
     * 我们这里自己命名对象innerSet_simTest2
     * 持久化了3个对象
     * 多了一个对innerSet的命名引用对象
     */
    @RequestMapping("/simTest2")
    public void simTest2(){
        SimTest2 simTest2 = new SimTest2();
        simTest2.setName("simTest2");
        Set<String> innerSet = redissonClient.getSet("innerSet_simTest2");
        for (int i = 0; i < 10; i++) {
            innerSet.add("xxxx" + i);
        }
        simTest2.setInnerSet(innerSet);
        simTest2 = rloClient.persist(simTest2);
        for (String element : simTest2.getInnerSet()){
            log.info("simTest {}", element);
        }
    }

    @RequestMapping("/simTest3")
    public void simTest3(){
        RMap<String, Set<SimTest2>> compositeMap = redissonClient.getMap("compositeMap");
        for (int i = 0; i < 10; i++) {
            Set<SimTest2> tempSet = new HashSet<>();
            for (int j = 0; j < 10; j++) {
                SimTest2 simTest2 = new SimTest2();
                simTest2.setName("simTest2_" + (i*100)+j );
                simTest2 = rloClient.persist(simTest2);
                tempSet.add(simTest2);
            }
            compositeMap.put("composite_key" + i, tempSet);
        }
    }
}
