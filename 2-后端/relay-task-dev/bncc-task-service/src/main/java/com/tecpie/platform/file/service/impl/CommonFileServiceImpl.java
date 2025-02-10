package com.tecpie.platform.file.service.impl;

import cn.hutool.core.date.DatePattern;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.file.api.command.query.CommonFileQueryCommand;
import com.tecpie.platform.file.entity.CommonFile;
import com.tecpie.platform.file.mapper.CommonFileMapper;
import com.tecpie.platform.file.service.CommonFileService;
import com.tecpie.platform.s3.utils.AwsS3FileUtil;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文件和图片表 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-07-23
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CommonFileServiceImpl extends BaseServiceImpl<CommonFileMapper, CommonFile> implements CommonFileService {

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.preUrl}")
    private String preUrl;

    @Value("${tecpie.systemContainer}")
    private Integer systemContainer;

    @Override
    public CommonFile selectExtensionById(Serializable id) {
        CommonFile entity = this.baseMapper.selectExtensionById(id);
        if (entity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("文件和图片表[%s]不存在", id));
        }
        return entity;
    }

    @Override
    public List<CommonFile> searchExtensionPageList(CommonFileQueryCommand command) {
        return this.baseMapper.searchExtensionPageList(command);
    }

    @Override
    public List<CommonFile> searchExtensionList(Integer businessType, Serializable businessId) {
        CommonFileQueryCommand command = new CommonFileQueryCommand();
        command.setBusinessType(businessType);
        command.setBusinessId(businessId);
        return this.searchExtensionPageList(command);
    }

    @Override
    public void updateBusinessId(List<CommonFile> fileList, Integer businessType, Serializable businessId) {
        if (CollectionUtils.isEmpty(fileList)) {
            return;
        }
        List<Serializable> fileIdList = fileList.stream().map(CommonFile::getId).collect(Collectors.toList());
        List<CommonFile> commonFileNewList = this.lambdaQuery().in(CommonFile::getId, fileIdList).list();
        // 数据库的文件
        Map<String, CommonFile> existFileMap = Maps.newHashMap();
        if (ObjectUtils.isNotEmpty(businessId)) {
            // 先根据业务ID和类型查询跟这个业务有关的数据
            List<CommonFile> commonFileList = this.lambdaQuery().eq(CommonFile::getBusinessType, businessType).eq(CommonFile::getBusinessId, businessId).list();
            if (!CollectionUtils.isEmpty(commonFileList)) {
                existFileMap = commonFileList.stream().collect(Collectors.toMap(e -> e.getId().toString(), e -> e));
            }
        }
        List<CommonFile> updateList = Lists.newArrayList();
        for (CommonFile entity : commonFileNewList) {
            CommonFile commonFile = existFileMap.get(entity.getId().toString());
            if (commonFile != null) {
                existFileMap.remove(entity.getId().toString());
                continue;
            }
            entity.setBusinessType(businessType);
            entity.setBusinessId(businessId);
            updateList.add(entity);
        }
        // 需要修改的文件
        if (!CollectionUtils.isEmpty(updateList)) {
            this.updateBatchById(updateList);
        }
        // 需要删除的文件
        if (existFileMap.size() > 0) {
            for (String id : existFileMap.keySet()) {
                CommonFile commonFile = existFileMap.get(id);
                // 删除文件
                AwsS3FileUtil.deleteFile(bucketName, commonFile.getFileName());
                // 删除数据
                this.removeById(Long.parseLong(id));
            }
        }
    }

    @Override
    public void deleteCommonFile(Serializable id) {
        CommonFile existEntity = this.baseMapper.selectById(id);
        if (existEntity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("文件和图片表[%s]不存在", id));
        }
        // 删除文件
        AwsS3FileUtil.deleteFile(bucketName, existEntity.getFileName());
        // 删除数据
        this.removeById(existEntity);
    }

    @Override
    public List<CommonFile> batchUpload(MultipartFile[] fileList) {
        List<CommonFile> commonFileList = Lists.newLinkedList();
        try {
            List<CommonFile> commonFileAddList = Lists.newLinkedList();
            for (MultipartFile file : fileList) {
                // 文件名称
                String fileName = file.getOriginalFilename();
                // 根据文件名是否存在
                CommonFile existFile = this.lambdaQuery().eq(CommonFile::getFileName, fileName).one();
                if (existFile != null) {
                    commonFileList.add(existFile);
                    continue;
                }
                CommonFile commonFile = new CommonFile();
                commonFile.setFileSize(file.getSize() / 1024 + " Kb");
                commonFile.setFileSource(systemContainer);
                commonFile.setFileTime(LocalDateTime.now());
                // 上传文件到对象存储
                fileName = fileName + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_MS_PATTERN));
                commonFile.setFileName(fileName);
                // 如果是内网-2，不上传BLOB数据，如果是外网-1，上传BLOB数据
                if (systemContainer == 1){
                    commonFile.setFileContent(file.getBytes());
                }
                // 上传S3
                AwsS3FileUtil.uploadFileLimit100M(file.getInputStream(), bucketName, fileName);
                // 获取文件连接
                String awsFileUrl = preUrl + "/api/v1/file/download/" + fileName;
                commonFile.setFileUrl(awsFileUrl);
                commonFileAddList.add(commonFile);
            }
            if (!CollectionUtils.isEmpty(commonFileAddList)) {
                this.saveBatch(commonFileAddList);
                commonFileList.addAll(commonFileAddList);
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return commonFileList;
    }
}