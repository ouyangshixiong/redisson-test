package com.example.entity;

import lombok.Data;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RCascadeType;
import org.redisson.api.annotation.RCascade;
import org.redisson.api.annotation.RId;

import java.util.List;

/**
 * @author alexouyang
 * @Date 2019-12-03
 */
@Data
public class DetachObject1 extends Calc {

    @RId
    private String name;


    @RCascade(RCascadeType.ALL)
    private List<Cycle2> cycle2List;

    @Override
    public boolean equals( Object other ){
        if( ! (other instanceof DetachObject1) ){
            return false;
        }else{
            return getName().equals(((DetachObject1) other).getName());
        }
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }


}
