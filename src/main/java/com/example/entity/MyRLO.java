package com.example.entity;

import lombok.Data;
import lombok.ToString;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.util.List;
import java.util.Map;

/**
 * @author alexouyang
 * @Date 2019-10-12
 */
@REntity
@Data
public class MyRLO{

    @RId
    private String persistId;

    private long id;

    private String name;

    private List<String> aList;

    private Map<String,String> aMap;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("persistId=").append(persistId).append("\t")
                .append("id=").append(id).append("\t")
                .append("name=").append(name).append("\t");
        if( aList != null ){
            sb.append("aList").append("\n");
            for( String element : aList ){
                sb.append("element=").append(element).append("\t");
            }
            sb.append("\n");
        }else{}
        if( aMap != null){
            sb.append("aMap").append("\n");
            for( String key : aMap.keySet() ){
                sb.append("key=").append(key).append("\t")
                        .append("value=").append(aMap.get(key)).append("\n");
            }
            sb.append("\n");
        }else{}
        return sb.toString();

    }

}
