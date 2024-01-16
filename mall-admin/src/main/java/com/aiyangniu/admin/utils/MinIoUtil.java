package com.aiyangniu.admin.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.aiyangniu.admin.config.MinIoConfig;
import com.aiyangniu.entity.model.bo.BucketPolicyConfigResult;
import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * MinIO对象存储工具类
 *
 * @author lzq
 * @date 2024/01/16
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MinIoUtil {

    private final MinIoConfig minIoConfig;
    private final MinioClient minioClient;

    /**
     * 查看存储bucket是否存在
     */
    public Boolean bucketExists() {
        boolean isExist;
        try {
            isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minIoConfig.getBucketName()).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return isExist;
    }

    /**
     * 创建存储bucket
     */
    public Boolean makeBucket() {
        try {
            // 创建存储桶并设置只读权限
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(minIoConfig.getBucketName()).build());
            BucketPolicyConfigResult result = createBucketPolicyConfig(minIoConfig.getBucketName());
            SetBucketPolicyArgs setBucketPolicyArgs = SetBucketPolicyArgs.builder()
                    .bucket(minIoConfig.getBucketName())
                    .config(JSONUtil.toJsonStr(result))
                    .build();
            minioClient.setBucketPolicy(setBucketPolicyArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 创建存储桶的访问策略，设置为只读权限
     */
    private BucketPolicyConfigResult createBucketPolicyConfig(String bucketName) {
        BucketPolicyConfigResult.Statement statement = BucketPolicyConfigResult.Statement.builder()
                .effect("Allow")
                .principal("*")
                .action("s3:GetObject")
                .resource("arn:aws:s3:::" + bucketName + "/*.**").build();
        return BucketPolicyConfigResult.builder()
                .version("2012-10-17")
                .statement(CollUtil.toList(statement))
                .build();
    }

    /**
     * 上传文件到存储桶
     */
    public void createBucketFolder(String objectName, MultipartFile file) throws Exception {
        // 使用putObject上传一个文件到存储桶中
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(minIoConfig.getBucketName())
                .object(objectName)
                .contentType(file.getContentType())
                .stream(file.getInputStream(), file.getSize(), ObjectWriteArgs.MIN_MULTIPART_SIZE).build();
        minioClient.putObject(putObjectArgs);
    }

    /**
     * 删除存储bucket
     */
    public Boolean removeBucket(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(minIoConfig.getBucketName()).object(objectName).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取全部bucket
     */
    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 列出存储桶中的所有对象
     */
    public Iterable<Result<Item>> listObjects() {
        boolean flag = bucketExists();
        if (flag) {
            return minioClient.listObjects(ListObjectsArgs.builder().bucket(minIoConfig.getBucketName()).build());
        }
        return null;
    }

    /**
     * 递归查询桶下对象
     */
    public Iterable<Result<Item>> recursiveListObjects() {
        boolean flag = bucketExists();
        if (flag) {
            return minioClient.listObjects(ListObjectsArgs.builder().bucket(minIoConfig.getBucketName()).recursive(true).build());
        }
        return null;
    }

    /**
     * 列出某个桶中的所有文件名
     * 文件夹名为空时，则直接查询桶下面的数据，否则就查询当前桶下对于文件夹里面的数据
     *
     * @param bucketName 桶名称
     * @param folderName 文件夹名
     * @param isDeep     是否递归查询
     */
    public Iterable<Result<Item>> getBucketAllFile(String bucketName, String folderName, Boolean isDeep) {
        if (!StringUtils.hasLength(folderName)) {
            folderName = "";
        }
        return minioClient.listObjects(
                ListObjectsArgs
                        .builder()
                        .bucket(bucketName)
                        .prefix(folderName + "/")
                        .recursive(isDeep)
                        .build());
    }
}
