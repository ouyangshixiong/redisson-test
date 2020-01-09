package com.example.entity;

import lombok.Data;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

/**
 * @author alexouyang
 * @Date 2019-12-02
 */
@REntity
@Data
public class SimTest3 extends Calc {
    @RId
    private String name;


    private SimGroup simGroup;
}
