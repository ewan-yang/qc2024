package com.tecpie.platform.s3.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.util.Date;

@Component
public class AwsS3FileUtil {

    private static Logger logger = LoggerFactory.getLogger(AwsS3FileUtil.class);

    private static AmazonS3 amazonS3Client;

    @Autowired
    public void setAmazonS3Client(AmazonS3 amazonS3Client) {
        AwsS3FileUtil.amazonS3Client = amazonS3Client;
    }

    /**
     * 普通上传，限制100M以内
     *
     * @param is
     * @param bucketName
     * @param objName
     */
    public static void uploadFileLimit100M(InputStream is, String bucketName, String objName) {
//    boolean exist = amazonS3Client.doesBucketExistV2(bucketName);
//    if (!exist) {
//      AwsS3BucketUtil.createBucket(bucketName);
//    }
        try {
            PutObjectRequest request = new PutObjectRequest(bucketName, objName, is,
                    new ObjectMetadata());
            // 设置公共读取
            request.withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3Client.putObject(request);
        } catch (Exception e) {
            logger.error("aws上传异常", e);
        }
    }

    /**
     * 分片上传
     *
     * @param filePath
     * @param bucketName
     * @param objName
     */
    public static void uploadFileBlock(String filePath, String bucketName, String objName) {
        //初始化一个分块上传，获取分块上传ID，桶名 + 对像名 + 分块上传ID 唯一确定一个分块上传
        try (FileInputStream is = new FileInputStream(filePath)) {
            if (!amazonS3Client.doesBucketExistV2(bucketName)) {
                AwsS3BucketUtil.createBucket(bucketName);
            }
            InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName,
                    objName);
            InitiateMultipartUploadResult initResult = amazonS3Client
                    .initiateMultipartUpload(initRequest);
            String uploadId = initResult.getUploadId();
            //分块上传的最小单位为16K，最后一块可以小于16K，每个分块都得标识一个唯一的分块partIndex
            //分块上传的最大单位100M
            int readLen;
            int buffSize = 10 * 1024 * 1024;
            byte[] buffer = new byte[buffSize];
            int partIndex = 1;
            while ((readLen = is.read(buffer, 0, buffSize)) != -1) {
                InputStream partStream = new ByteArrayInputStream(buffer);
                amazonS3Client.uploadPart(new UploadPartRequest().withBucketName(bucketName)
                        .withUploadId(uploadId).withInputStream(partStream)
                        .withKey(objName).withPartSize(readLen).withPartNumber(partIndex));
                partIndex++;
            }
        } catch (Exception e) {
            logger.error("分片上传异常", e);
        }
    }

    /**
     * 删除文件
     *
     * @param bucketName
     * @param objName
     */
    public static void deleteFile(String bucketName, String objName) {
        if (amazonS3Client.doesObjectExist(bucketName, objName)) {
            amazonS3Client.deleteObject(bucketName, objName);
        }
    }

    /**
     * 构建下载路径
     *
     * @param bucketName
     * @param objName
     * @param seconds    多久后过期
     * @return
     */
    public static String buildDownloadUrl(String bucketName, String objName, Long seconds) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
                bucketName, objName);
        generatePresignedUrlRequest.setExpiration(new Date(System.currentTimeMillis() + seconds));
        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.getPath();
    }

    /**
     * 构建下载路径，不设置过期时间
     *
     * @param bucketName
     * @param objName
     * @return
     */
    public static String buildDownloadUrl(String bucketName, String objName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
                bucketName, objName);
        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
        String path = url.toString();
        String https = path.substring(0, path.indexOf("?"));
        String substring = "http:" + https.substring("https".length());
        return substring;
    }

    /**
     * 下载文件
     *
     * @param bucketName
     * @param objName
     * @return
     */
    public static String downLoadFile(String bucketName, String objName) {
        S3Object object = amazonS3Client.getObject(bucketName, objName);
        S3ObjectInputStream objectContent = object.getObjectContent();
        try (FileOutputStream fos = new FileOutputStream(new File(objName))) {
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = objectContent.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            objectContent.close();
        } catch (Exception e) {
            logger.error("ASW S3文件下载异常", e);
        }
        return objName;
    }

    /**
     * 下载文件
     *
     * @param bucketName
     * @param objName
     * @return
     */
    public static S3ObjectInputStream downLoadFileStream(String bucketName, String objName) {
        S3Object object = amazonS3Client.getObject(bucketName, objName);
        S3ObjectInputStream objectContent = object.getObjectContent();
        return objectContent;
    }
}
