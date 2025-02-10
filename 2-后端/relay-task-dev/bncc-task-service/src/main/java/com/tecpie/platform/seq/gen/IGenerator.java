package com.tecpie.platform.seq.gen;

import java.util.Map;

/**
 * 序列生成器
 *
 * @author Tanzj
 * @date 2023/7/16 22:07
 */
public interface IGenerator {

    /**
     * 生成类型
     *
     * @return String
     */
    String getGeneratorType();

    /**
     * 生成序列
     *
     * @param format       生成规则
     * @param parameterMap 参数Map
     * @return String
     */
    String generate(String format, Map<String, String> parameterMap);

}
