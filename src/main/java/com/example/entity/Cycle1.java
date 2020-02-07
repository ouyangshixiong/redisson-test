package com.example.entity;

import lombok.Data;
import org.apache.jute.compiler.generated.Rcc;
import org.redisson.api.RCascadeType;
import org.redisson.api.annotation.RCascade;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.util.Set;

/**
 * @author alexouyang
 * @Date 2019-12-03
 */
@Data
@REntity
public class Cycle1 extends Calc {

    @RId
    private String name;

    @RCascade(RCascadeType.PERSIST)
    private Set<Cycle2> cycle2Set;

    @Override
    public boolean equals( Object other ){
        if( ! (other instanceof Cycle1) ){
            return false;
        }else{
            return getName().equals(((Cycle1) other).getName());
        }
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }

    @Override
    public String toString(){
        return null;
    }
}
