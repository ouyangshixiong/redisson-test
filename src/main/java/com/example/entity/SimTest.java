package com.example.entity;

import lombok.Data;
import org.redisson.api.RCascadeType;
import org.redisson.api.annotation.RCascade;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.util.Set;

/**
 * @author alexouyang
 * @Date 2019-11-19
 */
@REntity
@Data
public class SimTest extends Calc{

    @RId
    private String name;

    @RCascade(RCascadeType.PERSIST)
    private Set<String> innerSet;

    @Override
    public boolean equals( Object other ){
        if( ! (other instanceof SimTest) ){
            return false;
        }else{
            return getName().equals(((SimTest) other).getName());
        }
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }

}
