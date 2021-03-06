package com.example.service;

import org.redisson.api.*;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author alexouyang
 * @Date 2019-09-17
 */
public interface RedissonService<T> {

    RBatch getBatch();

    RTransaction getTransaction();

    String echo( String word );

    RAtomicLong getAtomicLong(String key);

    void putLong( String key, long value );

    Long getLong(String key);

    RLocalCachedMap getMap(String mapName );

    void destoryMap( String mapName );

    void putOneInRBucket( String key, T value );

    T getFromRBucket( String key );

    T persistRLO( T value );

    T getRLO( String key, Class clazz );

    boolean isRLO(T obj);

    boolean isExists( T obj );

    void registerRLO(Class cls);

    void deleteRLO( T obj );

    T attach( T obj );

    T merge( T obj );

    RReadWriteLock getReadWriteLock(String lockName);

}
