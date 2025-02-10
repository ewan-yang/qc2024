package com.tecpie.platform.seq.gen.impl;

import com.tecpie.platform.seq.gen.GeneratorTypeEnum;
import com.tecpie.platform.seq.gen.IGenerator;

import java.util.Map;

/**
 * 字符串生成器
 *
 * @author Tanzj
 * @date 2023/7/16 22:12
 */
public class StringGenerator implements IGenerator {

    private static final String TYPE = GeneratorTypeEnum.STRING.getValue();

    @Override
    public String getGeneratorType() {
        return TYPE;
    }

    @Override
    public String generate(String format, Map<String, String> parameterMap) {
        return format;
    }

}
