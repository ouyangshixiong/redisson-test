package com.example.controller;

import com.example.entity.MyRLO;
import com.example.service.RedissonService;
import org.redisson.api.RLiveObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author alexouyang
 * @Date 2019-10-12
 */
@RestController
public class RLOController {

    @Autowired
    RedissonService redissonService;

    @RequestMapping("/rlo_test_init")
    public void test1(){
        RLiveObjectService RLOService = redissonService.getRLiveObjectService();
        RLOService.registerClass(MyRLO.class);
        RLOService.persist(getMyRLO("rlo_1", "bean1"));
        RLOService.persist(getMyRLO("rlo_2", "bean2"));
        RLOService.persist(getMyRLO("rlo_3", "bean3"));
    }

    @RequestMapping("/rlo_test_modify_name")
    public void test2(String beanName){
        RLiveObjectService RLOService = redissonService.getRLiveObjectService();
        RLOService.registerClass(MyRLO.class);
        MyRLO myRLO = RLOService.get(MyRLO.class, "rlo_1");
        myRLO.setName(beanName);
    }

    @RequestMapping("/rlo_test_modify_list")
    public void test2List(String valueAdded){
        RLiveObjectService RLOService = redissonService.getRLiveObjectService();
        RLOService.registerClass(MyRLO.class);
        MyRLO myRLO = RLOService.get(MyRLO.class, "rlo_1");
        myRLO.getAList().add(valueAdded);
    }

    @RequestMapping("/rlo_test_modify_map")
    public void test2Map(String aModified){
        RLiveObjectService RLOService = redissonService.getRLiveObjectService();
        RLOService.registerClass(MyRLO.class);
        MyRLO myRLO = RLOService.get(MyRLO.class, "rlo_1");
        myRLO.getAMap().put("a",aModified);
    }

    /**
     * persistance 之后，继续引用该对象，怎么set都无效
     */
    @RequestMapping("/rlo_test_modify_map_notwork")
    public void test2MapNotWork(){
        RLiveObjectService RLOService = redissonService.getRLiveObjectService();
        RLOService.registerClass(MyRLO.class);
        MyRLO myRLO = getMyRLO("rlo_4", "bean4");
        RLOService.persist(myRLO);
//        MyRLO myRLO = RLOService.get(MyRLO.class, "rlo_1");
        Map<String,String> newMap = new HashMap<>();
        newMap.put("newkey","newValue");
        myRLO.setAMap(newMap);
    }

    @RequestMapping("/rlo_test_get")
    public void get(String persistId){
        RLiveObjectService RLOService = redissonService.getRLiveObjectService();
        RLOService.registerClass(MyRLO.class);
        MyRLO myRLO = RLOService.get(MyRLO.class, persistId);
        String name = myRLO.getName();
        System.out.println("bean name:" + name);
        List<String> aList = myRLO.getAList();
        for( String element : aList ){
            System.out.println("aList element " + element);
        }
        Map<String, String> aMap = myRLO.getAMap();
        for( String key : aMap.keySet() ){
            System.out.println("aMap key=" + key + " value=" + aMap.get(key));
        }
    }

    private MyRLO getMyRLO( String persistId, String beanName ){
        MyRLO myRLO = new MyRLO();
        myRLO.setId(1000L);
        myRLO.setName(beanName);
        List<String> aList = new ArrayList<>();
        aList.add("a");
        aList.add("b");
        aList.add("c");
        aList.add("d");
        myRLO.setAList(aList);
        Map<String,String> aMap = new HashMap<>();
        aMap.put("a","1");
        aMap.put("b","2");
        aMap.put("c","3");
        myRLO.setAMap(aMap);
        myRLO.setPersistId(persistId);
        return myRLO;
    }

}
