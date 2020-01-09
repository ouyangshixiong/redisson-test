package com.example.service;

import org.apache.dubbo.config.annotation.Service;
import org.redisson.api.*;
import org.redisson.api.BatchOptions.*;
import org.redisson.api.LocalCachedMapOptions.EvictionPolicy;
import org.redisson.api.LocalCachedMapOptions.ReconnectionStrategy;
import org.redisson.api.LocalCachedMapOptions.SyncStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author alexouyang
 * @Date 2019-09-17
 */
@Service
public class RedissonDAO<T> implements RedissonService<T>{

    RedissonClient redissonClient;

    RLiveObjectService rloClient;

    @Autowired
    RedissonDAO(RedissonClient redissonClient){
        this.redissonClient = redissonClient;
        rloClient = this.redissonClient.getLiveObjectService();
    }

    BatchOptions batchOptions = BatchOptions.defaults()
    // 指定执行模式
    //
    // ExecutionMode.REDIS_READ_ATOMIC - 所有命令缓存在Redis节点中，以原子性事务的方式执行。
    //
    // ExecutionMode.REDIS_WRITE_ATOMIC - 所有命令缓存在Redis节点中，以原子性事务的方式执行。
    //
    // ExecutionMode.IN_MEMORY - 所有命令缓存在Redisson本机内存中统一发送，但逐一执行（非事务）。默认模式。
    //
    // ExecutionMode.IN_MEMORY_ATOMIC - 所有命令缓存在Redisson本机内存中统一发送，并以原子性事务的方式执行。
    //
                .executionMode(ExecutionMode.IN_MEMORY_ATOMIC)

    // 告知Redis不用返回结果（可以减少网络用量）
                .skipResult()

    // 将写入操作同步到从节点
    // 同步到2个从节点，等待时间为1秒钟
                .syncSlaves(2, 1, TimeUnit.SECONDS)

    // 处理结果超时为2秒钟
                .responseTimeout(2, TimeUnit.SECONDS)

    // 命令重试等待间隔时间为2秒钟
                .retryInterval(2, TimeUnit.SECONDS)

    // 命令重试次数。仅适用于未发送成功的命令
                .retryAttempts(4);

    @Override
    public RBatch getBatch(){
        return redissonClient.createBatch(batchOptions);
    }


    TransactionOptions transactionOptions = TransactionOptions.defaults()
        // 设置参与本次事务的主节点与其从节点同步的超时时间。
        // 默认值是5秒。
            .syncSlavesTimeout(5, TimeUnit.SECONDS)

        // 处理结果超时。
        // 默认值是3秒。
            .responseTimeout(3, TimeUnit.SECONDS)

        // 命令重试等待间隔时间。仅适用于未发送成功的命令。
        // 默认值是1.5秒。
            .retryInterval(2, TimeUnit.SECONDS)

        // 命令重试次数。仅适用于未发送成功的命令。
        // 默认值是3次。
            .retryAttempts(3)

        // 事务超时时间。如果规定时间内没有提交该事务则自动滚回。
        // 默认值是5秒。
            .timeout(5, TimeUnit.SECONDS);

    @Override
    public RTransaction getTransaction(){
        return redissonClient.createTransaction(transactionOptions);
    }

    LocalCachedMapOptions options = LocalCachedMapOptions.defaults()
            // 用于淘汰清除本地缓存内的元素
            // 共有以下几种选择:
            // LFU - 统计元素的使用频率，淘汰用得最少（最不常用）的。
            // LRU - 按元素使用时间排序比较，淘汰最早（最久远）的。
            // SOFT - 元素用Java的WeakReference来保存，缓存元素通过GC过程清除。
            // WEAK - 元素用Java的SoftReference来保存, 缓存元素通过GC过程清除。
            // NONE - 永不淘汰清除缓存元素。
            .evictionPolicy(EvictionPolicy.NONE)
            // 如果缓存容量值为0表示不限制本地缓存容量大小
            .cacheSize(1000)
            // 以下选项适用于断线原因造成了未收到本地缓存更新消息的情况。
            // 断线重连的策略有以下几种：
            // CLEAR - 如果断线一段时间以后则在重新建立连接以后清空本地缓存
            // LOAD - 在服务端保存一份10分钟的作废日志
            //        如果10分钟内重新建立连接，则按照作废日志内的记录清空本地缓存的元素
            //        如果断线时间超过了这个时间，则将清空本地缓存中所有的内容
            // NONE - 默认值。断线重连时不做处理。
            .reconnectionStrategy(ReconnectionStrategy.NONE)
            // 以下选项适用于不同本地缓存之间相互保持同步的情况
            // 缓存同步策略有以下几种：
            // INVALIDATE - 默认值。当本地缓存映射的某条元素发生变动时，同时驱逐所有相同本地缓存映射内的该元素
            // UPDATE - 当本地缓存映射的某条元素发生变动时，同时更新所有相同本地缓存映射内的该元素
            // NONE - 不做任何同步处理
            .syncStrategy(SyncStrategy.INVALIDATE)
            // 每个Map本地缓存里元素的有效时间，默认毫秒为单位
            .timeToLive(10000)
            // 或者
            .timeToLive(10, TimeUnit.SECONDS)
            // 每个Map本地缓存里元素的最长闲置时间，默认毫秒为单位
            .maxIdle(10000)
            // 或者
            .maxIdle(10, TimeUnit.SECONDS);

//    @Override
//    public void putAtomicLong( String key, long value ){
//        AtomicLong atomicValue = new AtomicLong(value);
//        RLocalCachedMap<String, AtomicLong> map = redissonClient.getLocalCachedMap("test1",options);
//        map.put(key,atomicValue);
//    }
//
//    @Override
//    public Long getAtomicLong( String key){
//        AtomicLong value = null;
//        try {
//            RLocalCachedMap<String, AtomicLong> map = redissonClient.getLocalCachedMap("test1",options);
//            value = map.get(key);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//
//        return value.get();
//    }

    @Override
    public RAtomicLong getAtomicLong( String key){
        return redissonClient.getAtomicLong(key);
    }

    @Override
    public void putLong(String key, long value) {
        RLocalCachedMap<String, Long> map = redissonClient.getLocalCachedMap("test1",options);
        map.put(key,value);
    }

    @Override
    public Long getLong(String key) {
        RLocalCachedMap<String, Long> map = redissonClient.getLocalCachedMap("test1", options);
        return map.get(key);
    }

    @Override
    public String echo(String word) {
        return word;
    }


    @Override
    public RLocalCachedMap getMap(String mapName ){
        return redissonClient.getLocalCachedMap(mapName, options);
    }
    /*
     * 销毁map，节省内存
     * 如果不销毁，在redissonClient 被shutdown的时候也会被销毁
     */
    @Override
    public void destoryMap( String mapName ){
        this.getMap(mapName).destroy();
    }

    @Override
    public void putOneInRBucket( String key, T value ){
        RBucket<T> bucket = redissonClient.getBucket( key);
        bucket.set(value);
    }

    @Override
    public T getFromRBucket( String key ){
        RBucket<T> bucket = redissonClient.getBucket( key );
        return bucket.get();
    }

    @Override
    public T persistRLO( T value ){
        return rloClient.persist(value);
    }

    @Override
    public T getRLO( String key, Class clazz ){
        return (T)rloClient.get( clazz, key );
    }

    @Override
    public boolean isRLO(T obj){
        return rloClient.isLiveObject(obj);
    }

    @Override
    public boolean isExists( T obj ){
        return rloClient.isExists(obj);
    }

    @Override
    public void registerRLO(Class cls){
        rloClient.registerClass(cls);
    }

    @Override
    public void deleteRLO(T obj){
        rloClient.delete(obj);
    }

    @Override
    public T attach( T obj ){
        return rloClient.attach(obj);
    }

    @Override
    public T merge( T obj ){
        return rloClient.merge( obj );
    }

    @Override
    public RReadWriteLock getReadWriteLock(String lockName){
        return redissonClient.getReadWriteLock(lockName);
    }

}
