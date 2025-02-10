package com.tecpie.platform.s3.controller;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.s3.controller.resource.FileMeta;
import com.tecpie.platform.s3.utils.AwsS3BucketUtil;
import com.tecpie.platform.s3.utils.AwsS3FileUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * 文件 控制器实现
 *
 * @author tecpie
 * @since 2022-07-29
 */
@Slf4j
@RestController
@Tag(name = "文件接口定义")
@RequestMapping("/api/v1/file")
public class AwsS3RestController {

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.preUrl}")
    private String preUrl;

    /**
     * 创建桶
     */
    @Operation(summary = "创建桶")
    @GetMapping(value = "/createBucket")
    public Result<FileMeta> createBucket(@RequestParam("bucketName") String bucketName) {
        AwsS3BucketUtil.createBucket(bucketName);
        return Result.success();
    }

    /**
     * 上传文件
     */
    @Operation(summary = "上传单个文件")
    @PostMapping(value = "/upload")
    public Result<FileMeta> uploadFile(@RequestParam("file") MultipartFile file) {
        String name = file.getOriginalFilename();
        // 后缀名
        FileMeta fileMeta = new FileMeta();
        fileMeta.setAlt(name);
        fileMeta.setSize(file.getSize() / 1024 + " Kb");
        fileMeta.setType(file.getContentType());
        // 上传文件到对象存储
        String uuidName = name + "_" + UUID.randomUUID();
        try {
            AwsS3FileUtil.uploadFileLimit100M(file.getInputStream(), bucketName, uuidName);
//            DataTrans dataTrans = new DataTrans();
//            dataTrans.setSource(source);
//            dataTrans.setType(DataTransTypeEnum.ADD);
//            dataTrans.setFileName(uuidName);
            byte[] fileBytes = file.getBytes();
            List<Byte> byteList = new ArrayList<>(fileBytes.length);
            for (byte fileByte : fileBytes) {
                byteList.add(fileByte);
            }
            for (int i = 0; i <= fileBytes.length / 1048576; i++) {
                byte[] bytes = new byte[1048576];
                List<Byte> subList = byteList
                        .subList(i * 1048576, Math.min(byteList.size(), (i + 1) * 1048576));
                for (int j = 0; j < subList.size(); j++) {
                    bytes[j] = subList.get(j);
                }
//                dataTrans.setFile(bytes);
//                dataTrans.setId(null);
//                dataTransService.save(dataTrans);
            }
            // 获取文件连接
            String awsFileUrl = preUrl + "/relay-task-service/api/v1/file/download/" + uuidName;
            fileMeta.setUrl(awsFileUrl);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return Result.success(fileMeta);
    }

    /**
     * 上传文件
     */
    @Operation(summary = "批量上传文件")
    @PostMapping(value = "/batchUpload")
    public Result<List<FileMeta>> batchUpload(@RequestParam("fileList") MultipartFile[] fileList) {
        List<FileMeta> files = new LinkedList<>();
        for (MultipartFile file : fileList) {
            String name = file.getOriginalFilename();
            // 后缀名
            FileMeta fileMeta = new FileMeta();
            fileMeta.setAlt(name);
            fileMeta.setSize(file.getSize() / 1024 + " Kb");
            fileMeta.setType(file.getContentType());
            // 上传文件到对象存储
            String uuidName = name + "_" + UUID.randomUUID().toString();
            try {
                AwsS3FileUtil.uploadFileLimit100M(file.getInputStream(), bucketName, uuidName);
//                DataTrans dataTrans = new DataTrans();
//                dataTrans.setSource(source);
//                dataTrans.setType(DataTransTypeEnum.ADD);
//                dataTrans.setFileName(uuidName);
//                dataTrans.setFile(file.getBytes());
                byte[] fileBytes = file.getBytes();
                List<Byte> byteList = new ArrayList<>(fileBytes.length);
                for (byte fileByte : fileBytes) {
                    byteList.add(fileByte);
                }
                for (int i = 0; i <= fileBytes.length / 1048576; i++) {
                    byte[] bytes = new byte[1048576];
                    List<Byte> subList = byteList
                            .subList(i * 1048576, Math.min(byteList.size(), (i + 1) * 1048576));
                    for (int j = 0; j < subList.size(); j++) {
                        bytes[j] = subList.get(j);
                    }
//                    dataTrans.setFile(bytes);
//                    dataTrans.setId(null);
//                    dataTransService.save(dataTrans);
                }
                // 获取文件连接
                String awsFileUrl = preUrl + "/relay-task-service/api/v1/file/download/" + uuidName;
                fileMeta.setUrl(awsFileUrl);
                files.add(fileMeta);
            } catch (IOException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        return Result.success(files);
    }

    /**
     * 批量上传文件
     *
     * @param objName
     * @param response
     */
    @Operation(summary = "文件下载")
    @GetMapping("download/{objName}")
    public void downloadFile(@PathVariable("objName") String objName, HttpServletResponse response) {
        try {
            S3ObjectInputStream s3ObjectInputStream = AwsS3FileUtil
                    .downLoadFileStream(bucketName, objName);
            BufferedInputStream bis = new BufferedInputStream(s3ObjectInputStream);
            String fileName = objName.substring(objName.lastIndexOf("/") + 1, objName.lastIndexOf("_"));
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("content-disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            response.setHeader("Access-Control-Expose-Headers", "content-disposition");
            OutputStream os = response.getOutputStream();
            byte[] d = new byte[1024];
            int r;
            log.info("下载文件");
            while ((r = bis.read(d)) != -1) {
                os.write(d, 0, r);
            }
            os.close();
            bis.close();
            s3ObjectInputStream.close();
        } catch (Exception e) {
            log.error("下载文件异常", e);
        }
    }

}