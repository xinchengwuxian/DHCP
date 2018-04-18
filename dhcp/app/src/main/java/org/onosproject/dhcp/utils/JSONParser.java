/*
 * Fiberhome SDN Platform
 * Copyright (c)  Fiberhome Technologies.
 * 88,YouKeYuan Road,Hongshan District.,Wuhan,P.R.China,
 * Wuhan Research Institute of Post &amp; Telecommunication.
 * All rights reserved.
 */
package org.onosproject.dhcp.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

/**
 * 创建人: fengl.
 * 创建时间 2017-04-11 下午4:39
 * 功能描述: JSON解析器
 **/

public final class JSONParser {

    /**
     *
     * 创建人:fengl.
     * 创建时间:2017-04-11 18:53
     * 功能描述: JSON串转成TAPI对象
     * 参数及返回值说明:
     *
     * @param entityClass TAPI对象类型
     * @param jsonstr JSON串
     * @param <T> TAPI对象类型
     * @return API对象
     */
    public static <T> T toTapiObject(Class<T> entityClass, String jsonstr) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
        mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        T obj = null;
        try {
            obj = mapper.readValue(jsonstr, entityClass);
        } catch (IOException e) {
        }
        return obj;
    }


    /**
     *
     * 创建人:chenshaohui.
     * 创建时间:2017-04-12 15:33
     * 功能描述: java类转为json串
     * 参数及返回值说明:
     *
     * @param pojo java object
     * @return json串
     */
    public static String returnJsonString(Object pojo) {
        @SuppressWarnings("serial")
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
        mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        String rs = null;
        try {
            rs = mapper.writeValueAsString(pojo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return rs;
    }

}
