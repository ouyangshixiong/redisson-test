package com.example.controller;

import com.example.entity.SimGroup;
import com.example.entity.SimTest3;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alexouyang
 * @Date 2019-12-02
 */
@RestController
@Slf4j
public class EmbededRLOController {

    RedissonClient redissonClient;

    RLiveObjectService rloClient;

    public EmbededRLOController( RedissonClient redissonClient ){
        this.redissonClient = redissonClient;
        rloClient = redissonClient.getLiveObjectService();
    }

    /**
     * 不初始化，会get到null，跟java一样
     */
    @RequestMapping("/embeded_rlo")
    public void embededRLO(){
        SimTest3 simTest3 = new SimTest3();
        simTest3.setName("simTest3_0");
        simTest3 = rloClient.persist(simTest3);
        SimGroup simGroup = simTest3.getSimGroup();
        log.info("embeded_rlo {}", simGroup);
    }


    /**
     * 手动初始化，会报错
     */
    @RequestMapping("/embeded_rlo2")
    public void embededRLO2(){
        SimGroup simGroup = new SimGroup();
        simGroup.setName("simGroup3_1");
        simGroup = rloClient.persist(simGroup);
        SimTest3 simTest3 = new SimTest3();
        simTest3.setName("simTest3_1");
        simTest3.setSimGroup(simGroup);
        // 会报错，因为RLO对象的@RId是二进制插码实现的，而SimGroup的hashCode方法用到了@RId
        // 可以在simTest3 persist之后在set RLO对象
        simTest3 = rloClient.persist(simTest3);
//        simTest3.setSimGroup(simGroup);
        simGroup = simTest3.getSimGroup();
        log.info("embeded_rlo2 {}", simGroup);
    }
}
