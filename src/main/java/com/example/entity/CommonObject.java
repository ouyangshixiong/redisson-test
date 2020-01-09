package com.example.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author alexouyang
 * @Date 2019-11-12
 */
@Data
public class CommonObject implements Serializable {

    private String name;

    private int value;
}
