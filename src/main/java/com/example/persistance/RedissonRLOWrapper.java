package com.example.persistance;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RedissonClient;
import org.redisson.api.annotation.REntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author alexouyang
 * @Date 2019-11-08
 */
@Component
@Data
@Slf4j
public class RedissonRLOWrapper<E extends AbstractEntity> {

    private final RedissonClient redissonClient;

    private final RLiveObjectService rloClient;

    private List<AbstractEntity> dataListWrapper;

    private Map<String, AbstractEntity> dataMapWrapper;

    @Autowired
    RedissonRLOWrapper(RedissonClient redissonClient){
        this.redissonClient = redissonClient;
        rloClient = redissonClient.getLiveObjectService();
    }

    public <E extends AbstractEntity> void init( List<E> data, Class<E> cls  ) throws IOException{
        if( data == null || data.isEmpty() ){
            return;
        }else if( !cls.isAnnotationPresent(REntity.class) ){
            throw new IOException("Wappered object should be annotated with @REntity!");
        }else{
//            dataListWrapper = new ArrayList<>(data.size());
//            dataMapWrapper = new HashMap<>(data.size());
            dataListWrapper = redissonClient.getList(cls.getName()+"dataList");
            dataMapWrapper = redissonClient.getMap( cls.getName() + "dataMap" );
            for( AbstractEntity element : data ){
                String key = element.getPersistName();
                element = rloClient.persist(element);
                dataListWrapper.add(element);
                dataMapWrapper.put(key, element);
            }
        }
    }

    public <E extends AbstractEntity> boolean delete( E element, Class<E> cls ) throws IOException{
        if( element == null ){
            return false;
        }else if( !cls.isAnnotationPresent(REntity.class) ){
            throw new IOException("Wappered object should be annotated with @REntity!");
        }else{
            for(int i=0; i<dataListWrapper.size(); i++){
                if( dataListWrapper.get(i).equals(element) ){
                    log.warn("remove List ele {}", i);
                    dataListWrapper.remove(i);
                    break;
                }else{}
            }
            for( String key : dataMapWrapper.keySet() ){
                if( dataMapWrapper.get(key).equals(element) ){
                    log.warn("dataMapWrapper.get{} persistName={} persistName={} ",key, dataMapWrapper.get(key).getPersistName(), element.getPersistName());
                    log.warn("dataMapWrapper.get{} hashCode={} hashCode={}",key,dataMapWrapper.get(key).hashCode(),element.hashCode());
                    log.warn("remove Map ele key={}", key);
                    dataMapWrapper.remove(key);
                    break;
                }else{}
            }
            element = rloClient.get(cls,element.getPersistName());
            rloClient.delete(element);
            return true;
        }
    }

}
