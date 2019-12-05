package com.example.entity;

import lombok.Data;
import org.redisson.api.RCascadeType;
import org.redisson.api.annotation.RCascade;

import java.util.List;

/**
 * @author alexouyang
 * @Date 2019-12-03
 */
@Data
public class DetachObject1 extends Calc {

    private SimPool simPool;

    @RCascade(RCascadeType.PERSIST)
    private List<Cycle2> cycle2List;

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
