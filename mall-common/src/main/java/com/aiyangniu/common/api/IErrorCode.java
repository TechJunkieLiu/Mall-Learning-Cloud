package com.aiyangniu.common.api;

/**
 * API返回码接口
 *
 * @author lzq
 * @date 2023/09/21
 */
public interface IErrorCode {

    /**
     * 获取状态码
     *
     * @return 返回码
     */
    long getCode();

    /**
     * 获取信息
     *
     * @return 返回信息
     */
    String getMessage();
}
