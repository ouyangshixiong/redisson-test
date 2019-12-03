package com.example.entity;

import lombok.Data;
import org.redisson.api.annotation.REntity;

/**
 * @author alexouyang
 * @Date 2019-12-02
 */
@REntity
@Data
public class SimTest3 extends Calc {

    private SimGroup simGroup;
}
