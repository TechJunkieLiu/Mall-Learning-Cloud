package com.aiyangniu.admin.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import com.aiyangniu.admin.config.OssProperties;
import com.aiyangniu.admin.service.OssService;
import com.aiyangniu.common.exception.ApiException;
import com.aiyangniu.entity.model.bo.OssCallbackParam;
import com.aiyangniu.entity.model.bo.OssCallbackResult;
import com.aiyangniu.entity.model.bo.OssPolicyResult;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Oss对象存储管理实现类
 *
 * @author lzq
 * @date 2024/01/19
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OssServiceImpl implements OssService {

	@Override
	public OssPolicyResult policy() {
		// 创建OSSClient实例
		OSSClient ossClient = new OSSClient(OssProperties.END_POINT, OssProperties.ACCESS_KEY_ID, OssProperties.ACCESS_KEY_SECRET);
		log.info("OSSClient实例创建成功！");
		OssPolicyResult result = new OssPolicyResult();
		// 存储目录
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dir = OssProperties.DIR_PREFIX + sdf.format(new Date());
		// 签名有效期
		long expireEndTime = System.currentTimeMillis() + OssProperties.POLICY_EXPIRE * 1000;
		Date expiration = new Date(expireEndTime);
		// 文件大小
		long maxSize = OssProperties.MAX_SIZE * 1024 * 1024;
		// 回调
		OssCallbackParam callback = new OssCallbackParam();
		callback.setCallbackUrl(OssProperties.CALLBACK);
		callback.setCallbackBody("filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
		callback.setCallbackBodyType("application/x-www-form-urlencoded");
		// 提交节点
		String action = "http://" + OssProperties.BUCKET_NAME + "." + OssProperties.END_POINT;
		try {
			PolicyConditions policyConds = new PolicyConditions();
			policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, maxSize);
			policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
			String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
			byte[] binaryData = postPolicy.getBytes("utf-8");
			String policy = BinaryUtil.toBase64String(binaryData);
			String signature = ossClient.calculatePostSignature(postPolicy);
			String callbackData = BinaryUtil.toBase64String(JSONUtil.parse(callback).toString().getBytes("utf-8"));
			// 返回结果
			result.setAccessKeyId(ossClient.getCredentialsProvider().getCredentials().getAccessKeyId());
			result.setPolicy(policy);
			result.setSignature(signature);
			result.setDir(dir);
			result.setCallback(callbackData);
			result.setHost(action);
		} catch (Exception e) {
			log.error("签名生成失败", e);
		}
		return result;
	}

	@Override
	public OssCallbackResult callback(HttpServletRequest request) {
		OssCallbackResult result = new OssCallbackResult();
		String filename = request.getParameter("filename");
		filename = "http://".concat(OssProperties.BUCKET_NAME).concat(".").concat(OssProperties.END_POINT).concat("/").concat(filename);
		result.setFilename(filename);
		result.setSize(request.getParameter("size"));
		result.setMimeType(request.getParameter("mimeType"));
		result.setWidth(request.getParameter("width"));
		result.setHeight(request.getParameter("height"));
		return result;
	}

	@Override
	public String upload(InputStream inputStream, String module, String fileName) {
		// 创建OSSClient实例
		OSSClient ossClient = new OSSClient(OssProperties.END_POINT, OssProperties.ACCESS_KEY_ID, OssProperties.ACCESS_KEY_SECRET);
		log.info("OSSClient实例创建成功！");
		try {
			// 判断OSS实例是否存在，如果存在则获取，不存在则创建（访问权限设置）
			if (!ossClient.doesBucketExist(OssProperties.BUCKET_NAME)) {
				ossClient.createBucket(OssProperties.BUCKET_NAME);
				log.info("Bucket存储空间【{}】创建成功！", OssProperties.BUCKET_NAME);
				ossClient.setBucketAcl(OssProperties.BUCKET_NAME, CannedAccessControlList.PublicRead);
				log.info("【{}】存储空间访问权限设置为公共读成功！", OssProperties.BUCKET_NAME);
			}
			String folder = new DateTime().toString("yyyy/MM/dd");
			fileName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));
			// 文件根路径
			String key = module + "/" + folder + "/" + fileName;
			PutObjectRequest putObjectRequest = new PutObjectRequest(OssProperties.BUCKET_NAME, key, inputStream);
			ossClient.putObject(putObjectRequest);
			log.info("OSS文件上传成功！");
			// 阿里云文件绝对路径
			String endPoint = OssProperties.END_POINT.substring(OssProperties.END_POINT.lastIndexOf("//") + 2);
			// 返回文件的访问路径
			return "https://" + OssProperties.BUCKET_NAME + "." + endPoint + "/" + key;
		} catch (OSSException oe) {
			log.error("OSSException 文件上传失败：{}", oe);
			throw new ApiException(oe);
		} catch (ClientException ce) {
			log.error("ClientException 文件上传失败：{}", ExceptionUtils.getStackTrace(ce));
			throw new ApiException(ce);
		} finally {
			if (ossClient != null) {
				ossClient.shutdown();
				log.info("关闭ossClient！");
			}
		}
	}

	@Override
	public void remove(String url) {
		// 创建OSSClient实例
		OSSClient ossClient = new OSSClient(OssProperties.END_POINT, OssProperties.ACCESS_KEY_ID, OssProperties.ACCESS_KEY_SECRET);
		log.info("OSSClient实例创建成功！");
		try {
			String endPoint = OssProperties.END_POINT.substring(OssProperties.END_POINT.lastIndexOf("//") + 1);
			// 文件名（服务器上的文件路径）
			String host = "https://" + OssProperties.BUCKET_NAME + "." + endPoint + "/";
			String objectName = url.substring(host.length());
			// 删除文件或目录（如果要删除目录，目录必须为空）
			ossClient.deleteObject(OssProperties.BUCKET_NAME, objectName);
			log.info("{}文件删除成功！", objectName);
		} catch (OSSException oe) {
			log.error("OSSException 文件删除失败：{}", oe);
			throw new ApiException(oe);
		} catch (ClientException ce) {
			log.error("ClientException 文件删除失败：{}", ExceptionUtils.getStackTrace(ce));
			throw new ApiException(ce);
		} finally {
			if (ossClient != null) {
				ossClient.shutdown();
				log.info("关闭ossClient！");
			}
		}
	}
}
