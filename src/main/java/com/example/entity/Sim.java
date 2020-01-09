package com.example.entity;

import com.example.controller.ApplicationContextProvider;
import com.example.controller.SnowFlake;
import com.example.service.RedissonService;
import lombok.Data;
import org.redisson.api.RMap;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author alexouyang
 * @Date 2019-10-31
 */
@REntity
@Data
public class Sim extends Calc {

    @RId
    private String name;

    private List<SimGroup> simGroupList;

    public Sim(){

    }

    public Sim(RMap<Long,Long> counterMap){
        super(counterMap);
    }

    public Sim(Long counterId){
        super(counterId);
    }


    @Override
    public boolean equals( Object other ){
        if( ! (other instanceof Sim) ){
            return false;
        }else{
            return getName().equals(((Sim) other).getName());
        }
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }


    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("sim count=").append(queryCount());
        for(SimGroup simGroup: getSimGroupList()){
            sb.append(simGroup.toString());
        }
        return sb.toString();
    }
}
