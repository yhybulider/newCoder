package com.whllow.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CommunityUtil {


    //生成随机的字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    //MD5加密算法：单向加密不能解密，每次加载同一个密码得到的结果是一样的，（和hash算法很像）
    //简单密码的加密结果比较简单，数据库中salt和密码组合起来的加密结果比较安全
    public static  String md5(String key){
        if(StringUtils.isBlank(key))
            return null;
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getJSONSrting(int code, String msg, Map<String,Object> map){

        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        if(map!=null){
            Set<String> keys = map.keySet();
            for(String k:keys){
                json.put(k,map.get(k));
            }
        }
    return json.toJSONString();
    }

    public static String getJSONSrting(int code, String msg){
        return getJSONSrting(code,msg,null);
    }

    public static String getJSONSrting(int code){
        return getJSONSrting(code,null,null);
    }





}
