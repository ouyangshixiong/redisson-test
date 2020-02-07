package com.example.entity;

import lombok.Data;
import org.redisson.api.RCascadeType;
import org.redisson.api.annotation.RCascade;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

/**
 * @author alexouyang
 * @Date 2020-01-20
 */
@REntity
@Data
public class AdvRLO2 {

    @RId
    private String name;

    @RCascade(RCascadeType.PERSIST)
    private AdvRLO0 advRLO0;

    @RCascade(RCascadeType.PERSIST)
    private AdvRLO3 advRLO3;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(" name=").append(getName()).append(" advRLO0.name=").append(getAdvRLO0()==null?null:getAdvRLO0().getName())
                .append(" testAdvRLO3.name=").append(getAdvRLO3()==null?null:getAdvRLO3().getName());
        return sb.toString();
    }
}
