package com.example.entity;

import lombok.Data;

/**
 * @author alexouyang
 * @Date 2019-12-03
 */
@Data
public class Cycle2 extends Calc {

    private Cycle1 cycle1;

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
