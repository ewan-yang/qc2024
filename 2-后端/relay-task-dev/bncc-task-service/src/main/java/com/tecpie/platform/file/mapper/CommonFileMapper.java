package com.tecpie.platform.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.file.api.command.query.CommonFileQueryCommand;
import com.tecpie.platform.file.entity.CommonFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * 文件和图片表 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-07-23
 */
@Repository
public interface CommonFileMapper extends BaseMapper<CommonFile> {

    /**
     * 获取详情信息
     *
     * @param id
     * @return CommonFile
     */
    CommonFile selectExtensionById(@Param("id") Serializable id);


    /**
     * 检索详情列表
     *
     * @param command
     * @return List<CommonFile>
     */
    List<CommonFile> searchExtensionPageList(@Param("condition") CommonFileQueryCommand command);

    List<CommonFile> selectForUpdate(@Param("source") Integer source);

}