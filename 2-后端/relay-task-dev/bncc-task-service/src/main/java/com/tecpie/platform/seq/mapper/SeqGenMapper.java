package com.tecpie.platform.seq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.seq.api.command.query.SeqGenQueryCommand;
import com.tecpie.platform.seq.entity.SeqGen;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * 序列表 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-07-17
 */
@Repository
public interface SeqGenMapper extends BaseMapper<SeqGen> {

    /**
     * 获取详情信息
     *
     * @param id
     * @return SeqGen
     */
    SeqGen selectExtensionById(@Param("id") Serializable id);


    /**
     * 检索详情列表
     *
     * @param command
     * @return List<SeqGen>
     */
    List<SeqGen> searchExtensionPageList(@Param("condition") SeqGenQueryCommand command);

}