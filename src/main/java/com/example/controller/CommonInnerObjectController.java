package com.example.controller;

import com.example.entity.CommonObject;
import com.example.entity.SimTest4;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alexouyang
 * @Date 2019-12-26
 */
@RestController
@Slf4j
public class CommonInnerObjectController {

    RedissonClient redissonClient;

    RLiveObjectService rloClient;

    public CommonInnerObjectController( RedissonClient redissonClient ){
        this.redissonClient = redissonClient;
        rloClient = redissonClient.getLiveObjectService();
    }

    @RequestMapping("/inner_common")
    public void innerCommon(){
        CommonObject commonObject = new CommonObject();
        commonObject.setName("common1");
        commonObject.setValue(1);
        SimTest4 simTest4 = new SimTest4();
        simTest4.setName("simtest4");
        simTest4.setCommonObject(commonObject);
        SimTest4 temp = rloClient.persist(simTest4);
        log.info("temp:{}",temp);
    }


}
