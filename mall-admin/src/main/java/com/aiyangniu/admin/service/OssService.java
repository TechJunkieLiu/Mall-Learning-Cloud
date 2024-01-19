package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.bo.OssCallbackResult;
import com.aiyangniu.entity.model.bo.OssPolicyResult;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

/**
 * Oss对象存储管理接口
 *
 * @author lzq
 * @date 2024/01/19
 */
public interface OssService {

    /**
     * Oss上传策略生成
     *
     * @return 上传策略
     */
    OssPolicyResult policy();

    /**
     * Oss上传成功回调
     *
     * @param request 上传请求
     * @return 上传成功回调
     */
    OssCallbackResult callback(HttpServletRequest request);

    /**
     * Oss文件上传
     *
     * @param inputStream 输入流
     * @param module 模块
     * @param originalFilename 原始文件名
     * @return 文件上传路径
     */
    String upload(InputStream inputStream, String module, String originalFilename);

    /**
     * Oss文件删除
     *
     * @param url 要删除的文件路径
     */
    void remove(String url);
}
