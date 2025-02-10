package com.tecpie.platform.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tecpie.platform.user.business.data.api.resource.SysLovLineResource;
import com.tecpie.platform.user.business.data.api.resource.SysLovResource;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 告警服务Util类
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/7/21 16:42
 */
public class AnalysisAlarmUtil {

    /**
     * 获取字典Map,以code为Key
     */
    public static Map<String, String> getLovLileCodeMap(List<SysLovResource> lovList) {
        Map<String, String> resultMap = Maps.newHashMap();
        // 获取行
        List<SysLovLineResource> lovLineList = getLovLineList(lovList);
        for (SysLovLineResource item : lovLineList) {
            resultMap.put(item.getCode(), item.getName());
        }
        return resultMap;
    }

    private static List<SysLovLineResource> getLovLineList(List<SysLovResource> lovList) {
        if (CollectionUtils.isEmpty(lovList)) {
            return Lists.newArrayList();
        }
        SysLovResource sysLov = lovList.get(0);
        List<SysLovLineResource> lovLineList = sysLov.getLovLineList();
        if (CollectionUtils.isEmpty(lovLineList)) {
            return Lists.newArrayList();
        }
        return lovLineList;
    }

}
