package com.example.entity;

import lombok.Data;

import java.util.Set;

/**
 * @author alexouyang
 * @Date 2019-12-03
 */
@Data
public class Cycle1 extends Calc {

    private Set<Cycle2> cycle2Set;

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

    @Override
    public String toString(){
        return null;
    }
}
