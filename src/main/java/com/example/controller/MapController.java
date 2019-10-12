package com.example.controller;

import com.example.entity.MyObject;
import com.example.service.RedissonService;
import org.redisson.api.RFuture;
import org.redisson.api.RLocalCachedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author alexouyang
 * @Date 2019-09-25
 */
@RestController
public class MapController {

    @Autowired
    RedissonService redissonService;

    @RequestMapping("/test1")
    public void putTest1(){
        long ct1 = System.currentTimeMillis();
        Map<Integer,String> stringMap = redissonService.getMap("put_test_1");
        long ct2 = System.currentTimeMillis();
        for (int i = 0; i <10000 ; i++) {
            String value = stringMap.get(i);
            if( value == null ){
                value = new String("init");
            }
            value = "value" + i;
            stringMap.put(i, value);
        }
        long ct3 = System.currentTimeMillis();
        System.err.printf("init simple string time cost {%d}, put time cost %d \n",(ct2-ct1), (ct3-ct2));
    }

    @RequestMapping("/test2")
    public void putTest2(){
        long ct1 = System.currentTimeMillis();
        Map<Integer, MyObject> objectMap = redissonService.getMap("put_test_2");
        long ct2 = System.currentTimeMillis();
        for (int i = 0; i <10000 ; i++) {
            MyObject value = objectMap.get(i);
            if( value == null ){
                value = MyObject.builder()
                        .value1("1").value2("2").value3("3").value4("4").value5("5").value6("6").value7("7").value8("8").value9("9").value10("10")
                        .value11("1").value12("2").value13("3").value14("4").value15("5").value16("6").value17("7").value18("8").value19("9").value20("10")
                        .value21("1").value22("2").value23("3").value24("4").value25("5").value26("6").value27("7").value28("8").value29("9").value30("10")
                        .value31("1").value32("2").value33("3").value34("4").value35("5").value36("6").value37("7").value38("8").value39("9").value40("10")
                        .value41("1").value42("2").value43("3").value44("4").value45("5").value46("6").value47("7").value48("8").value49("9").value50("10")
                        .build();
            }
            value.setValue1("2");
            objectMap.put(i, value);
        }
        long ct3 = System.currentTimeMillis();
        System.err.printf("init MyObject time cost {%d}, put time cost %d \n",(ct2-ct1), (ct3-ct2));
    }

    @RequestMapping("/test3")
    /**
     * Async
     */
    public void putTest3() throws Exception{
        List<RFuture<MyObject>> futureList = new ArrayList<>();
        long ct1 = System.currentTimeMillis();
        RLocalCachedMap<Integer, MyObject> objectMap = redissonService.getMap("put_test_3");
        long ct2 = System.currentTimeMillis();
        for (int i = 0; i <10000 ; i++) {
            MyObject value = objectMap.get(i);
            if( value == null ){
                value = MyObject.builder()
                        .value1("1").value2("2").value3("3").value4("4").value5("5").value6("6").value7("7").value8("8").value9("9").value10("10")
                        .value11("1").value12("2").value13("3").value14("4").value15("5").value16("6").value17("7").value18("8").value19("9").value20("10")
                        .value21("1").value22("2").value23("3").value24("4").value25("5").value26("6").value27("7").value28("8").value29("9").value30("10")
                        .value31("1").value32("2").value33("3").value34("4").value35("5").value36("6").value37("7").value38("8").value39("9").value40("10")
                        .value41("1").value42("2").value43("3").value44("4").value45("5").value46("6").value47("7").value48("8").value49("9").value50("10")
                        .build();
            }
            value.setValue1("2");
            RFuture<MyObject> future = objectMap.putAsync(i, value);
            futureList.add(future);
        }
        // 同步
        for( RFuture future : futureList ){
            future.get();
        }
        long ct3 = System.currentTimeMillis();
        System.err.printf("init MyObject time cost {%d}, async put time cost %d \n",(ct2-ct1), (ct3-ct2));
    }

    @RequestMapping("/fasttest3")
    /**
     * fastAsync
     */
    public void fastPutTest3() throws Exception{
        List<RFuture<Boolean>> futureList = new ArrayList<>();
        long ct1 = System.currentTimeMillis();
        RLocalCachedMap<Integer, MyObject> objectMap = redissonService.getMap("put_fast_test_3");
        long ct2 = System.currentTimeMillis();
        for (int i = 0; i <10000 ; i++) {
            MyObject value = objectMap.get(i);
            if( value == null ){
                value = MyObject.builder()
                        .value1("1").value2("2").value3("3").value4("4").value5("5").value6("6").value7("7").value8("8").value9("9").value10("10")
                        .value11("1").value12("2").value13("3").value14("4").value15("5").value16("6").value17("7").value18("8").value19("9").value20("10")
                        .value21("1").value22("2").value23("3").value24("4").value25("5").value26("6").value27("7").value28("8").value29("9").value30("10")
                        .value31("1").value32("2").value33("3").value34("4").value35("5").value36("6").value37("7").value38("8").value39("9").value40("10")
                        .value41("1").value42("2").value43("3").value44("4").value45("5").value46("6").value47("7").value48("8").value49("9").value50("10")
                        .build();
            }
            value.setValue1("2");
            RFuture<Boolean> future = objectMap.fastPutAsync(i, value);
            futureList.add(future);
        }
        // 同步
        for( RFuture future : futureList ){
            future.get();
        }
        long ct3 = System.currentTimeMillis();
        System.err.printf("init MyObject time cost {%d}, async fast put time cost %d \n",(ct2-ct1), (ct3-ct2));
    }


    @RequestMapping("/test4")
    /**
     * putAll
     */
    public void putTest4(){
        long ct1 = System.currentTimeMillis();
        RLocalCachedMap<Integer, MyObject> objectMap = redissonService.getMap("put_test_4");
        long ct2 = System.currentTimeMillis();
        Map<Integer, MyObject> temp = new HashMap<>();
        for (int i = 0; i <10000 ; i++) {
            MyObject value = objectMap.get(i);
            if( value == null ){
                value = MyObject.builder()
                        .value1("1").value2("2").value3("3").value4("4").value5("5").value6("6").value7("7").value8("8").value9("9").value10("10")
                        .value11("1").value12("2").value13("3").value14("4").value15("5").value16("6").value17("7").value18("8").value19("9").value20("10")
                        .value21("1").value22("2").value23("3").value24("4").value25("5").value26("6").value27("7").value28("8").value29("9").value30("10")
                        .value31("1").value32("2").value33("3").value34("4").value35("5").value36("6").value37("7").value38("8").value39("9").value40("10")
                        .value41("1").value42("2").value43("3").value44("4").value45("5").value46("6").value47("7").value48("8").value49("9").value50("10")
                        .build();
            }
            value.setValue1("2");
            temp.put(i, value);
        }
        objectMap.putAll(temp);
        long ct3 = System.currentTimeMillis();
        System.err.printf("init MyObject time cost {%d}, batch put time cost %d \n",(ct2-ct1), (ct3-ct2));
    }
}
