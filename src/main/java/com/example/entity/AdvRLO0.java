package com.example.entity;

import lombok.Data;
import org.redisson.api.RCascadeType;
import org.redisson.api.annotation.RCascade;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.util.List;

/**
 * @author alexouyang
 * @Date 2020-01-20
 */
@REntity
@Data
public class AdvRLO0 {

    @RId
    private String name;

    @RCascade(RCascadeType.PERSIST)
    private List<AdvRLO1> testAdvRLO1List;

    @RCascade(RCascadeType.PERSIST)
    private AdvRLO2 testAdvRLO2Object;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(" name=").append(getName()).append(" testAdvRLO1List.size=").append(getTestAdvRLO1List()==null?null:getTestAdvRLO1List().size())
                .append(" testAdvRLO2Object.name=").append(getTestAdvRLO2Object()==null?null:getTestAdvRLO2Object().getName());
        return sb.toString();
    }
}
