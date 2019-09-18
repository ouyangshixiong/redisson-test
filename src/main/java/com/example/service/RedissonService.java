package com.example.service;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author alexouyang
 * @Date 2019-09-17
 */
public interface RedissonService {

    String echo( String word );

    void putAtomicLong( String key, long value );

    Long getAtomicLong(String key);
}
