package com.example.entity;

import lombok.Data;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.util.List;
import java.util.Map;

/**
 * @author alexouyang
 * @Date 2019-10-12
 */
@REntity
@Data
public class MyRLO {

    @RId
    private String persistId;

    private long id;

    private String name;

    private List<String> aList;

    private Map<String,String> aMap;

}
