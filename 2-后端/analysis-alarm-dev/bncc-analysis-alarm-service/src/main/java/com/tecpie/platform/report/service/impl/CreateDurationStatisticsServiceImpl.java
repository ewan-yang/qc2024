package com.tecpie.platform.report.service.impl;

import com.google.common.collect.Maps;
import com.tecpie.platform.common.feign.user.SysUserFeignClient;
import com.tecpie.platform.common.util.ExcelTransfer;
import com.tecpie.platform.report.api.command.query.CreateDurationPageQueryCommand;
import com.tecpie.platform.report.api.command.query.CreateDurationQueryCommand;
import com.tecpie.platform.report.api.resource.CreateDurationStatisticsResource;
import com.tecpie.platform.report.api.resource.CreateDurationTotalResource;
import com.tecpie.platform.report.controller.assembler.resource.CreateDurationStatisticsResourceAssembler;
import com.tecpie.platform.report.entity.CreateDurationStatistics;
import com.tecpie.platform.report.mapper.CreateDurationStatisticsMapper;
import com.tecpie.platform.report.service.CreateDurationStatisticsService;
import com.tecpie.platform.user.business.organization.api.command.query.SysUserQueryCommand;
import com.tecpie.platform.user.business.organization.api.resource.SysUserResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 创建时长数据统计 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-08-03
 */
@Slf4j
@Service
public class CreateDurationStatisticsServiceImpl implements CreateDurationStatisticsService {

    private CreateDurationStatisticsMapper mapper;
    private SysUserFeignClient sysUserFeignClient;
    private ExcelTransfer excelTransfer;

    @Autowired
    public void setCreateDurationStatisticsServiceImpl(CreateDurationStatisticsMapper mapper, SysUserFeignClient sysUserFeignClient,
                                                       ExcelTransfer excelTransfer) {
        this.mapper = mapper;
        this.sysUserFeignClient = sysUserFeignClient;
        this.excelTransfer = excelTransfer;
    }

    @Override
    public List<CreateDurationStatistics> searchExtensionPageList(CreateDurationQueryCommand command) {
        command.initDateParam();
        // 统计非已取消的
        command.setExecuteStatusList(List.of("110","120","130","140","161"));
        return this.mapper.searchExtensionPageList(command);
    }

    @Override
    public CreateDurationTotalResource getCreateDurationTotal(CreateDurationQueryCommand command) {
        // 查询统计数据
        command.initDateParam();
        // 统计非已取消的
        command.setExecuteStatusList(List.of("110","120","130","140","161"));
        List<CreateDurationStatistics> createDurationList = this.mapper.searchExtensionPageList(command);
        // 获取当前登录用户信息
        Map<String, String> userMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(createDurationList)) {
            List<Long> userIdList = createDurationList.stream().map(CreateDurationStatistics::getTaskSource).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
            SysUserQueryCommand userQueryCommand = new SysUserQueryCommand();
            userQueryCommand.setIds(userIdList);
            List<SysUserResource> sysUserResourceList = sysUserFeignClient.getUserListByIds(userQueryCommand).getData();
            if (!CollectionUtils.isEmpty(sysUserResourceList)) {
                userMap = sysUserResourceList.stream().collect(Collectors.toMap(e -> e.getId().toString(), SysUserResource::getName));
            }
        }
        // 统计信息对象
        CreateDurationTotalResource totalResource = new CreateDurationTotalResource();
        // 初始化数据
        totalResource.initData(CreateDurationStatisticsResourceAssembler.INSTANCE.parseList(createDurationList), userMap);
        return totalResource;
    }

    @Override
    public void createDurationStatisticsExport(CreateDurationPageQueryCommand command, HttpServletResponse response) {
        List<CreateDurationStatistics> entityList = this.searchExtensionPageList(command.getCondition());
        List<CreateDurationStatisticsResource> list = CreateDurationStatisticsResourceAssembler.INSTANCE.parseList(entityList);
        try {
            excelTransfer.exportExcel(response,list,"创建时长统计列表","sheet1", CreateDurationStatisticsResource.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}