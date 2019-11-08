package com.example.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonList;
import org.redisson.api.RCascadeType;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.annotation.RCascade;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alexouyang
 * @Date 2019-11-07
 */
@REntity
@Slf4j
@Data
public class RLOCollectionWrapper<T> {

    @RId
    private String wrapperName;

    private Integer pageSize = 10;

    @RCascade(RCascadeType.PERSIST)
    private List<T> innerList;

//    /**
//     * 强制private
//     * @param rloList
//     */
//    private void setInnerList( List<T> rloList ){
//        this.innerList = rloList;
//    }
//
//    private List<T> getInnerList(){
//        return this.innerList;
//    }
//
//    private void setWrapperName(String wrapperName){
//        this.wrapperName = wrapperName;
//    }
//
//    private String getWrapperName(){
//        return this.wrapperName;
//    }

    public void putByPage(RLiveObjectService rloClient, String wrapperName, List<T> rloList ){
        RLOCollectionWrapper currentData = rloClient.get(RLOCollectionWrapper.class, wrapperName);
        if( currentData == null ){
            RLOCollectionWrapper abstractWrapper = new RLOCollectionWrapper();
            abstractWrapper.setWrapperName(wrapperName);
            abstractWrapper.setInnerList(new ArrayList<>());
            rloClient.persist(abstractWrapper);
            log.info("putByPage init");
        }else{
            if( rloList == null || rloList.isEmpty() ){
                return;
            }else{
                //计算分页数
                Integer pageNumber = rloList.size() / pageSize;
                if( rloList.size() % pageSize != 0 ){
                    pageNumber++;
                }else{}
                for(int i=0; i<pageNumber; i++){
                    RLOCollectionWrapper pageWrapper = new RLOCollectionWrapper();
                    pageWrapper.setWrapperName(wrapperName+"page" + i);
                    pageWrapper.setInnerList(rloList.subList(i*pageSize,(i+1)*pageSize));
                    pageWrapper = rloClient.persist(pageWrapper);
                    currentData.getInnerList().addAll(pageWrapper.getInnerList());
                    //清除临时的innerlist和pageWrapper对象；不会删除T
                    rloClient.delete(pageWrapper.getInnerList());
                    rloClient.delete(pageWrapper);
                    log.info("putByPage page" + (i+1) );
                }
            }
        }


    }
}
