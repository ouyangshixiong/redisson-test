package com.example.entity;

import lombok.Data;
import org.redisson.api.RCascadeType;
import org.redisson.api.annotation.RCascade;
import org.redisson.api.annotation.REntity;

import java.util.Set;

/**
 * @author alexouyang
 * @Date 2019-11-19
 */
@REntity
@Data
public class SimTest extends Calc{

    @RCascade(RCascadeType.PERSIST)
    private Set<String> innerSet;

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
