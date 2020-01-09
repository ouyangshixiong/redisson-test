package com.example.entity;

import lombok.Data;
import org.redisson.api.annotation.RId;

/**
 * @author alexouyang
 * @Date 2019-12-03
 */
@Data
public class Cycle2 extends Calc {

    @RId
    private String name;

    private Cycle1 cycle1;

    @Override
    public boolean equals( Object other ){
        if( ! (other instanceof Cycle2) ){
            return false;
        }else{
            return getName().equals(((Cycle2) other).getName());
        }
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }
}
