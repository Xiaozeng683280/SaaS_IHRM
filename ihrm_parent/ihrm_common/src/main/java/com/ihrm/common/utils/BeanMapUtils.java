package com.ihrm.common.utils;

import org.springframework.cglib.beans.BeanMap;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanMapUtils {

    /**
     * 将对象属性转化为map结合
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key+"", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 将map集合中的数据转化为指定对象的同名属性中
     */
    public static <T> T mapToBean(Map<String, Object> map,Class<T> clazz) throws Exception {
        T bean = clazz.newInstance();
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }


    public static List<Map<String, Object>> activitiResult(List<?> objs) {
        // 用于存放多个对象的集合
        List<Map<String, Object>> pdResult = new ArrayList<>();
        // 遍历方法参数中的集合
        for (Object obj : objs) {
            // 用于封装单个对象 get 方法返回值的 Map 集合
            Map<String, Object> pdMap = new HashMap<>();
            // 通过反射获取该对象的方法对象数组
            Method[] methods = obj.getClass().getMethods();
            // 遍历方法对象数组
            for (Method method : methods) {
                // 获取方法名称
                String methodName = method.getName();
                // 判断该方法是否名称不为 null ，并且名称是以 get 开头，满足条件进入 if 中
                if (methodName != null && methodName.startsWith("get")) {
                    // 设置方法的访问权限
                    method.setAccessible(true);
                    try {
                        // 将方法名的 get 前缀去掉，并增加 pd 前缀
                        String pdKey = "pb".concat(methodName.substring(3));
                        // 将 get 方法的名称作为 Map 的 key，将返回值作为 value 进行封装
                        pdMap.put(pdKey, method.invoke(obj, null));
                    } catch (Exception e) {
                        //输出异常,由于使得后台过乱，所以关掉
//                        e.printStackTrace();
                    }
                }
            }
            // 将封装好的 Map 集合添加到 List 集合中
            pdResult.add(pdMap);
        }
        return pdResult;
    }

}
