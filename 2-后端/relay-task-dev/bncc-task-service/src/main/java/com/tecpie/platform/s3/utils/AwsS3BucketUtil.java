package com.tecpie.platform.s3.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AwsS3BucketUtil {

    private static Logger logger = LoggerFactory.getLogger(AwsS3FileUtil.class);

    private static AmazonS3 amazonS3Client;

    @Autowired
    public void setAmazonS3Client(AmazonS3 amazonS3Client) {
        AwsS3BucketUtil.amazonS3Client = amazonS3Client;
    }

    /**
     * 创建桶
     *
     * @param bucketName
     * @return
     */
    public static void createBucket(String bucketName) {
        if (!amazonS3Client.doesBucketExistV2(bucketName)) {
            amazonS3Client.createBucket(bucketName);
        }
    }

    /**
     * 删除桶
     *
     * @param bucketName
     */
    public static void deleteBucket(String bucketName) {
        if (amazonS3Client.doesBucketExistV2(bucketName)) {
            amazonS3Client.deleteBucket(bucketName);
        }
    }

    /**
     * 查看桶权限
     *
     * @param bucketName
     * @return
     */
    public static String searchBucketAcl(String bucketName) {
        AccessControlList bucketAcl = amazonS3Client.getBucketAcl(bucketName);
        return bucketAcl.toString();
    }

    /**
     * 设置桶权限
     *
     * @param bucketName
     * @param acl
     */
    public static void setBucketAcl(String bucketName, CannedAccessControlList acl) {
        amazonS3Client.setBucketAcl(bucketName, acl);
    }
}
