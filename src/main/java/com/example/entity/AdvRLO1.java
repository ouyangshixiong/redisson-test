package com.example.entity;

import lombok.Data;
import org.redisson.api.RCascadeType;
import org.redisson.api.annotation.RCascade;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author alexouyang
 * @Date 2020-01-20
 */
@REntity
@Data
public class AdvRLO1 {

    @RId
    private String name;

    @RCascade(RCascadeType.PERSIST)
    private Map<String, AdvRLO2> advRLO2Map;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(" name=").append(getName()).append(" advRLO2.name=");
        if ( getAdvRLO2Map() != null && !getAdvRLO2Map().isEmpty() ){
            Iterator<Entry<String, AdvRLO2>> itr = getAdvRLO2Map().entrySet().iterator();
            while( itr.hasNext() ){
                Entry<String, AdvRLO2> entry = itr.next();
                sb.append(" AdvRLO1 inner map, key=").append(entry.getKey()).append(" value=").append(entry.getValue()==null?null:entry.getValue().getName());
            }
        }
        return sb.toString();
    }
}
