package com.aiyangniu.common.utils;

import org.springframework.beans.BeanUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 表单页面数据封装类（泛型），配合自定义的javabean使用，用于数据回显
 *
 * @author lzq
 * @date 2023/11/02
 */
public class FormBeanUtil {

    public static <T> T fillFormBean(Class<T> clazz, HttpServletRequest request){
        try {
            // 创建需要类型的bean
            T bean = clazz.newInstance();
            // 将 key-value 的值映射到 bean 中的属性
            BeanUtils.copyProperties(bean, request.getParameterMap());
            return bean;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
