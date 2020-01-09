package com.example.entity;

import lombok.Data;
import org.redisson.api.annotation.RId;

import java.util.Set;

/**
 * @author alexouyang
 * @Date 2019-12-03
 */
@Data
public class Cycle1 extends Calc {

    @RId
    private String name;

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
