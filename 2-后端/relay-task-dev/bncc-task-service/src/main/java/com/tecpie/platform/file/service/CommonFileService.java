package com.tecpie.platform.file.service;

import com.tecpie.platform.file.api.command.query.CommonFileQueryCommand;
import com.tecpie.platform.file.entity.CommonFile;
import com.tecpie.platform.file.mapper.CommonFileMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * 文件和图片表 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-07-23
 */
public interface CommonFileService extends IBaseService<CommonFileMapper, CommonFile> {

    /**
     * 获取详情信息
     *
     * @param id
     * @return CommonFile
     */
    CommonFile selectExtensionById(Serializable id);

    /**
     * 检索详情列表
     *
     * @param command
     * @return List<CommonFile>
     */
    List<CommonFile> searchExtensionPageList(CommonFileQueryCommand command);

    /**
     * 根据业务类型和业务ID获取文件
     *
     * @param businessType 业务类型
     * @param businessId   业务ID
     * @return List<CommonFile>
     */
    List<CommonFile> searchExtensionList(Integer businessType, Serializable businessId);

    /**
     * 更新文件表业务ID
     *
     * @param fileList   文件IdList
     * @param businessType 业务类型
     * @param businessId   业务ID
     */
    void updateBusinessId(List<CommonFile> fileList, Integer businessType, Serializable businessId);

    /**
     * 删除文件
     *
     * @param id
     */
    void deleteCommonFile(Serializable id);

    /**
     * 批量上传反馈文件
     *
     * @param fileList 文件List
     * @return List<CommonFile>
     */
    List<CommonFile> batchUpload(MultipartFile[] fileList);

}