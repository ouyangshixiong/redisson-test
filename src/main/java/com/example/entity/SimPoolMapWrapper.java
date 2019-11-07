package com.example.entity;

import lombok.Data;
import org.redisson.api.RCascadeType;
import org.redisson.api.annotation.RCascade;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.util.Map;

/**
 * @author alexouyang
 * @Date 2019-11-07
 */
@REntity
@Data
public class SimPoolMapWrapper {

    @RId
    String name;

    @RCascade(RCascadeType.ALL)
    Map<String, SimPool> simPoolMap;

}
