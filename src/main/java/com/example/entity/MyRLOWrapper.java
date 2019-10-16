package com.example.entity;

import lombok.Data;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author alexouyang
 * @Date 2019-10-16
 */
@REntity
@Data
public class MyRLOWrapper {

    @RId
    private String persistId;

    private ConcurrentHashMap<String, MyRLO> data;

}
