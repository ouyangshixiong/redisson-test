package com.example.entity;

import com.example.controller.ApplicationContextProvider;
import com.example.controller.SnowFlake;
import com.example.service.RedissonService;
import lombok.Data;
import org.redisson.api.RMap;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * @author alexouyang
 * @Date 2019-10-31
 */
@REntity
@Data
public class SimGroup extends Calc {

    @RId
    private String name;

    private Set<SimPool> simPoolSet;

    private Set<SimGroupInPool> simGroupInPoolSet;

    public SimGroup(){
    }

    public SimGroup(RMap<Long,Long> counterMap){
        super(counterMap);
    }

    public SimGroup(Long counterId){
        super(counterId);
    }

//    @Override
//    public boolean equals( Object other ){
//        if( ! (other instanceof SimGroup) ){
//            return false;
//        }else{
//            return getName().equals(((SimGroup) other).getName());
//        }
//    }
//
//    @Override
//    public int hashCode(){
//        return name.hashCode();
//    }


    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("simGroup count=").append(queryCount());
        for(SimPool simPool : getSimPoolSet()){
            sb.append(simPool.toString());
        }
        for(SimGroupInPool simGroupInPool : getSimGroupInPoolSet()){
            sb.append(simGroupInPool.toString());
        }
        return sb.toString();
    }
}
