package com.example.entity;

import lombok.Data;
import org.redisson.api.RCascadeType;
import org.redisson.api.annotation.RCascade;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.util.List;

/**
 * @author alexouyang
 * @Date 2019-10-31
 */
@REntity
@Data
public class Sim extends Calc {


    @RCascade(RCascadeType.ALL)
    private List<SimGroup> simGroupList;


    @Override
    public boolean equals( Object other ){
        if( ! (other instanceof Calc) ){
            return false;
        }else{
            return name.equals(((Calc) other).getName());
        }
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }
}
