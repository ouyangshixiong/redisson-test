package com.example.persistance;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

/**
 * @author alexouyang
 * @Date 2019-11-11
 */
@REntity
@Data
@Slf4j
public class AbstractEntity{

    @RId
    private String persistName;

//    @Override
//    public boolean equals(Object obj) {
//        if( obj == null ){
//            return false;
//        }else if( !(obj instanceof AbstractEntity) ){
//            return false;
//        }else{
//            AbstractEntity other = (AbstractEntity)obj;
//            if ( other.getPersistName().equalsIgnoreCase(getPersistName())){
//                return true;
//            }else{
//                return false;
//            }
//        }
//
//    }
//
//    @Override
//    public int hashCode(){
//        log.warn("hashCode={}",getPersistName().hashCode());
//        return getPersistName().hashCode();
//    }
}
