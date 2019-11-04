package com.example.controller;

import com.example.entity.MyRLO;
import com.example.service.RedissonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author alexouyang
 * @Date 2019-10-12
 */
@RestController
@Slf4j
public class RLOController {

    @Autowired
    RedissonService<MyRLO> redissonService;

    @RequestMapping("/rlo_test1")
    public void test1(){
        String persistId = "rlo_key_1";
        redissonService.registerRLO(MyRLO.class);
        MyRLO myRLO1 = redissonService.getRLO(persistId, MyRLO.class);
        if( myRLO1 == null ){
            myRLO1 = getMyRLO(persistId, "bean1");
            if(redissonService.isRLO(myRLO1)){
                log.info("myRLO1 is rlo obj");
            }else{
                log.info("myRLO1 is not rlo obj");
            }
            myRLO1 = redissonService.persistRLO( myRLO1);
            log.info("persist myRLO1");
        }else{
            log.info("myRLO1 is not null");
        }

        boolean isExists = redissonService.isExists(myRLO1);
        log.info("isExists!" + isExists);
        if( isExists ){
            log.info("before modify c=" + myRLO1.getAMap().get("c"));
            myRLO1.getAMap().put("c","RandomInt:" + new Random().nextInt(100));
            myRLO1 = redissonService.getRLO(persistId, MyRLO.class);
            log.info("after modify c=" + myRLO1.getAMap().get("c"));
        }else{}

    }

    @RequestMapping("/rlo_test2")
    public void test2(){
        String persistId = "rlo_key_2";
        redissonService.registerRLO(MyRLO.class);
        MyRLO myRLO2 = redissonService.getRLO(persistId, MyRLO.class);
        if( myRLO2 == null ){
            myRLO2 = getMyRLO(persistId, "bean2");
            redissonService.persistRLO( myRLO2);
            log.info("persist myRLO2");
        }else{
            if(redissonService.isRLO(myRLO2)){
                log.info("myRLO2 is rlo obj");
            }else{
                log.info("myRLO2 is not rlo obj");
            }
            redissonService.deleteRLO(myRLO2);
            log.info("myRLO2 deleted");
        }

    }

    @RequestMapping("/rlo_test3")
    public void test3(){
        String persistId = "rlo_key_3";
        redissonService.registerRLO(MyRLO.class);
        MyRLO myRLO3 = redissonService.getRLO(persistId, MyRLO.class);
        if( myRLO3 == null ){
            myRLO3 = getMyRLO(persistId, "bean3");
            redissonService.persistRLO( myRLO3);
            log.info("persist myRLO3");
        }else{}
        myRLO3 = getMyRLO(persistId, "bean3");
        myRLO3.setAList(Arrays.asList("100","101"));
        myRLO3 = redissonService.attach(myRLO3);
        log.info("aList=" + myRLO3.getAList());
    }

//    /**
//     * persistance 之后，继续引用该对象，怎么set都无效
//     */
//    @RequestMapping("/rlo_test_modify_map_notwork")
//    public void test2MapNotWork(){
//        RLiveObjectService RLOService = redissonService.getRLiveObjectService();
//        RLOService.registerClass(MyRLO.class);
//        MyRLO myRLO = getMyRLO("rlo_4", "bean4");
//        RLOService.persist(myRLO);
////        MyRLO myRLO = RLOService.get(MyRLO.class, "rlo_1");
//        Map<String,String> newMap = new HashMap<>();
//        newMap.put("newkey","newValue");
//        myRLO.setAMap(newMap);
//    }
//
//    @RequestMapping("/rlo_test_get")
//    public void get(String persistId){
//        RLiveObjectService RLOService = redissonService.getRLiveObjectService();
//        RLOService.registerClass(MyRLO.class);
//        MyRLO myRLO = RLOService.get(MyRLO.class, persistId);
//        String name = myRLO.getName();
//        System.out.println("bean name:" + name);
//        List<String> aList = myRLO.getAList();
//        for( String element : aList ){
//            System.out.println("aList element " + element);
//        }
//        Map<String, String> aMap = myRLO.getaMap();
//        for( String key : aMap.keySet() ){
//            System.out.println("aMap key=" + key + " value=" + aMap.get(key));
//        }
//    }
//
//    /**
//     * MyRLOMap继承了ConcurrentHashMap这种实现是RLO不支持的
//     * @return
//     */
//    @RequestMapping("/rlo_ref_init")
//    public String reference(Integer length){
//        long bt = System.currentTimeMillis();
//        StringBuilder sb = new StringBuilder();
//        RLiveObjectService RLOService = redissonService.getRLiveObjectService();
//        RLOService.registerClass(MyRLO.class);
//        RLOService.registerClass(MyRLOMap.class);
//        MyRLOMap<String, MyRLO> myRLOMap = new MyRLOMap();
//        myRLOMap.setPersistId("myRloMap1");
//        for(int i=0; i<length; i++){
//            MyRLO myRLO = getMyRLO("obj"+i, "bean"+i);
//            RLOService.persist(myRLO);
//            myRLOMap.put("key"+i, myRLO);
//        }
//        RLOService.persist(myRLOMap);
//        System.out.println("MyRLOMap's size=" + myRLOMap.size());
//        MyRLOMap<String, MyRLO> myRLOMap2 =RLOService.get(MyRLOMap.class, "myRloMap1");
//        System.out.println("MyRLOMap2's size=" + myRLOMap2.size());
//        String timeCost =  "timecost " + (System.currentTimeMillis() - bt) + "ms";
//        System.out.println("timeCost=" + timeCost);
//        return timeCost;
//    }
//
//    /**
//     * Redis只有一个Map对象，而且Value是1
//     * @return
//     */
//    @RequestMapping("/rlo_ref_get")
//    public String reference(){
//        StringBuilder sb = new StringBuilder();
//        RLiveObjectService RLOService = redissonService.getRLiveObjectService();
//        RLOService.registerClass(MyRLO.class);
//        RLOService.registerClass(MyRLOMap.class);
//        MyRLOMap<String, MyRLO> myRLOMap = RLOService.get(MyRLOMap.class, "myRloMap1");
//        sb.append("persistId=").append(myRLOMap.getPersistId());
//        sb.append("length=").append(myRLOMap.size());
//        for( String element : myRLOMap.keySet() ){
//            MyRLO myRLO = (MyRLO) myRLOMap.get(element);
//            sb.append(myRLO.toString());
//        }
//        return sb.toString();
//    }
//
//    /**
//     * 会报null，应该是RLO不能嵌套
//     * @return
//     */
//    @RequestMapping("/rlo_ref_wrapper")
//    public String wrapper(){
//        StringBuilder sb = new StringBuilder();
//        RLiveObjectService RLOSerivce = redissonService.getRLiveObjectService();
//        RLOSerivce.registerClass(MyRLO.class);
//        RLOSerivce.registerClass(MyRLOWrapper.class);
//        MyRLOWrapper wrapper = new MyRLOWrapper();
//        wrapper.setPersistId("wrapper1");
//        ConcurrentHashMap<String, MyRLO> data = new ConcurrentHashMap<>();
//        for( int i=0; i<10; i++ ){
//            data.put("map"+i, getMyRLO("bean"+i, "beanName" + i));
//        }
//        wrapper.setData(data);
//        RLOSerivce.persist(wrapper);
//        MyRLOWrapper temp = RLOSerivce.get( MyRLOWrapper.class, "wrapper1");
//        ConcurrentHashMap<String, MyRLO> tempData = temp.getData();
//        for (int i = 0; i <tempData.size() ; i++) {
//            sb.append(data.get(i).toString());
//        }
//        return sb.toString();
//    }
//
//    @RequestMapping("/rlo_simple_wrapper")
//    public String simpleWrapper(){
//        StringBuilder sb = new StringBuilder();
//        RLiveObjectService RLOSerivce = redissonService.getRLiveObjectService();
//        RLOSerivce.registerClass(MyRLOWrapper.class);
//        MyRLOWrapper2 wrapper = new MyRLOWrapper2();
//        wrapper.setPersistId("wrapper2");
//        HashMap<String, String> data = new HashMap<>();
//        for( int i=0; i<10; i++ ){
//            data.put("key"+i, "value"+i);
//        }
//        wrapper.setData(data);
//        RLOSerivce.persist(wrapper);
//        MyRLOWrapper2 temp = RLOSerivce.get( MyRLOWrapper2.class, "wrapper2");
//        //这里不是HashMap了，是RedissonMap
//        Map<String, String> tempData = temp.getData();
//        for (String key : tempData.keySet()) {
//            sb.append("key=").append(key).append(" value=").append(tempData.get(key));
//        }
//        return sb.toString();
//    }


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
