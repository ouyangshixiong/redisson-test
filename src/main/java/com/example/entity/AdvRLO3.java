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
public class AdvRLO3 {

    @RId
    private String name;

    @RCascade(RCascadeType.PERSIST)
    private AdvRLO3 testAdvRLO3;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(" name=").append(getName()).append(" testAdvRLO3.name=").append(getTestAdvRLO3() ==null?null: getTestAdvRLO3().getName());
        return sb.toString();
    }
}
