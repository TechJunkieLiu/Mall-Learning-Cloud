package com.aiyangniu.common.exception;

import com.aiyangniu.common.api.IErrorCode;

/**
 * 断言处理类，用于抛出各种API异常
 *
 * @author lzq
 * @date 2023/09/22
 */
public class Asserts {

    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }
}
