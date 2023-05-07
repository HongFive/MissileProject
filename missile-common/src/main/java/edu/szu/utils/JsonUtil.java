package edu.szu.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 *
 * @className json处理工具
 */
public class JsonUtil {

 //json字符串转对象
    public static <T> T toObject(String json,Class<T> tClass){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json,tClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 对象转json字符串
    public static String toString(Object json){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    // json数组转map对象
    public static <K,V> Map<K,V> toMap(String json,Map<K,V> map){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json,map.getClass());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}