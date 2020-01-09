package com.example.entity;

import lombok.Data;
import org.redisson.api.annotation.RId;

/**
 * @author alexouyang
 * @Date 2019-12-26
 */
@Data
public class SimTest4 extends Calc {
    @RId
    private String name;

    CommonObject commonObject;
}
