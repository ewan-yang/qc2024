package com.tecpie.platform.file.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tecpie.library.common.business.entity.BaseEntity;
import com.tecpie.platform.file.entity.CommonFile;
import com.tecpie.platform.file.mapper.CommonFileMapper;
import com.tecpie.platform.file.service.CommonFileService;
import com.tecpie.platform.s3.utils.AwsS3FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据传输 服务类实现
 *
 * @author tecpie
 * @since 2022-07-29
 */
@Slf4j
@Component
@Transactional(rollbackFor = Exception.class)
public class FileTransJob {

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${tecpie.systemContainer}")
    private Integer systemContainer;

    @Autowired
    private CommonFileService commonFileService;

    @Autowired
    private CommonFileMapper commonFileMapper;

    @Scheduled(cron = "*/10 * * * * *")
    @Transactional(rollbackFor = Exception.class)
    public void transData() {
        log.info("文件同步开始....");
        // 如果是外网不需要进行文件同步
        if (systemContainer == 1) {
            log.info("当前为外网环境，不需要文件同步....");
            return;
        }
        log.info("当前为内网环境，需要进行文件同步....");
        // 将外网数据，即FileSource=1的数据获取，并在内网进行上传
        List<CommonFile> commonFileList = commonFileMapper.selectList(Wrappers.lambdaQuery(CommonFile.class)
                .eq(CommonFile::getFileSource, 1)
                .isNotNull(CommonFile::getFileContent));
        if (!commonFileList.isEmpty()) {
            // 清空BLOB
            commonFileService.lambdaUpdate()
                    .in(BaseEntity::getId,
                            commonFileList.stream().map(BaseEntity::getId).collect(Collectors.toList())).set(CommonFile::getFileContent, null).update();
        }
        Map<String, List<CommonFile>> fileMap = new HashMap<>();
        if (CollectionUtils.isEmpty(commonFileList)) {
            return;
        }
        for (CommonFile commonFile : commonFileList) {
            List<CommonFile> fileList = fileMap
                    .getOrDefault(commonFile.getFileName(), new ArrayList<>());
            fileList.add(commonFile);
            fileMap.put(commonFile.getFileName(), fileList);
        }
        fileMap.forEach((fileName, dataTrans) -> {
            List<Byte> fileBytes = new ArrayList<>();
            dataTrans.stream().map(CommonFile::getFileContent).filter(Objects::nonNull).forEachOrdered(file -> {
                for (byte b : file) {
                    fileBytes.add(b);
                }
            });
            byte[] bytes = new byte[fileBytes.size()];
            for (int i = 0; i < fileBytes.size(); i++) {
                bytes[i] = fileBytes.get(i);
            }
            if (bytes.length > 0) {
                AwsS3FileUtil.uploadFileLimit100M(new ByteArrayInputStream(bytes), bucketName, fileName);
            }
        });
    }
}