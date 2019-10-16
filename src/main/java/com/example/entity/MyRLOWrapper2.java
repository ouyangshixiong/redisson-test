package com.example.entity;

import lombok.Data;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author alexouyang
 * @Date 2019-10-16
 */
@REntity
@Data
public class MyRLOWrapper2 {

    @RId
    private String persistId;

    private Map<String, String> data;

}
