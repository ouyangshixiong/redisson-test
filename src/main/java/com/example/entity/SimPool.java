package com.example.entity;

import lombok.Data;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

/**
 * @author alexouyang
 * @Date 2019-10-31
 */
@REntity
@Data
public class SimPool extends Calc implements Comparable<Object> {

    private int id;

    @Override
    public int compareTo(Object other) {
        if (other == null) {
            return -1;
        }else{}
        if( !(other instanceof SimPool) ){
            return -1;
        }else{}
        SimPool o2 = (SimPool)other;
        return getId() - o2.getId();
    }

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
