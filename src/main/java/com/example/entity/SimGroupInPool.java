package com.example.entity;

import com.example.controller.ApplicationContextProvider;
import com.example.controller.SnowFlake;
import com.example.service.RedissonService;
import lombok.Data;
import org.redisson.api.RMap;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import javax.annotation.PostConstruct;

/**
 * @author alexouyang
 * @Date 2019-10-31
 */
@REntity
@Data
public class SimGroupInPool extends Calc implements Comparable<Object> {

    @RId
    private String name;

    private int id;

    public SimGroupInPool(){

    }

    public SimGroupInPool(RMap<Long,Long> counterMap){
        super(counterMap);
    }

    public SimGroupInPool(Long counterId){
        super(counterId);
    }

    @Override
    public int compareTo(Object other) {
        if (other == null) {
            return -1;
        }else{}
        if( !(other instanceof SimGroupInPool) ){
            return -1;
        }else{}
        SimGroupInPool o2 = (SimGroupInPool)other;
        return getId()-o2.getId();
    }

//    @Override
//    public boolean equals( Object other ){
//        if( ! (other instanceof SimGroupInPool) ){
//            return false;
//        }else{
//            return getName().equals(((SimGroupInPool) other).getName());
//        }
//    }
//
//    @Override
//    public int hashCode(){
//        return name.hashCode();
//    }

    @Override
    public String toString(){
        return "simGroupInPool count=" + queryCount();
    }
}
