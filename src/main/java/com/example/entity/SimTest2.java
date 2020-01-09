package com.example.entity;

import lombok.Data;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.util.Set;

/**
 * @author alexouyang
 * @Date 2019-11-19
 */
@REntity
@Data
public class SimTest2 extends Calc{

    @RId
    private String name;

    private Set<String> innerSet;

    @Override
    public boolean equals( Object other ){
        if( ! (other instanceof SimTest2) ){
            return false;
        }else{
            return getName().equals(((SimTest2) other).getName());
        }
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }
}
