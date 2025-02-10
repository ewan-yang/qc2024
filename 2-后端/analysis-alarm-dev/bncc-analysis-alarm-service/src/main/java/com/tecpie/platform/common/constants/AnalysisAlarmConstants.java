package com.tecpie.platform.common.constants;

/**
 * 预告警 常量类
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/7/21 13:12
 */
public class AnalysisAlarmConstants {

    /**
     * 规则编号生成规则
     */
    public static final String RULE_CODE_GEN = "ADVANCE_RULE_CODE_SEQ";

    /**
     * 下达风险预警规则编号
     */
    public static final String CARRIER_RELEASE_RULE_CODE = "GZ0000001";

    /**
     * 下达超时预警规则编号
     */
    public static final String RELEASE_TIME_OUT_RULE_CODE = "GZ0000002";

    /**
     * 重复停电预警 10至35kV
     */
    public static final String RPC_LT_35KV_RULE_CODE = "GZ0000003";

    /**
     * 重复停电预警 35kV及与以上
     */
    public static final String RPC_GTE_35KV_RULE_CODE = "GZ0000004";

    /**
     * 反馈超期预警规则编号
     */
    public static final String FEEDBACK_TIME_OUT_RULE_CODE = "GZ0000005";

}
