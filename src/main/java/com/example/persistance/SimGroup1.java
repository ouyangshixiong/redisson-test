package com.example.persistance;

import lombok.Data;
import org.redisson.api.annotation.REntity;

import java.util.List;

/**
 * @author alexouyang
 * @Date 2019-11-11
 */
@REntity
@Data
public class SimGroup1 extends AbstractEntity {

    private String simGroupName;

    private List<Integer> someRelation;
}
