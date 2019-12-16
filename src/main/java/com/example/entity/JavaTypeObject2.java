package com.example.entity;

import lombok.Data;
import org.redisson.api.RCascadeType;
import org.redisson.api.annotation.RCascade;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.util.Set;

/**
 * @author alexouyang
 * @Date 2019-12-10
 */
@REntity
@Data
public class JavaTypeObject2 {

    @RId
    private String name;

    private Set<MyObject> myObjectSet;
}
