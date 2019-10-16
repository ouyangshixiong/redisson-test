package com.example.entity;

import lombok.Data;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author alexouyang
 * @Date 2019-10-14
 */
@REntity
@Data
public class MyRLOMap<String, T extends MyRLO> extends ConcurrentHashMap<String, T> {

    @RId
    private java.lang.String persistId;
}
