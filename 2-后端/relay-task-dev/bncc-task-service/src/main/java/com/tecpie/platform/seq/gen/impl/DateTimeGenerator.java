package com.tecpie.platform.seq.gen.impl;

import com.tecpie.platform.seq.gen.GeneratorTypeEnum;
import com.tecpie.platform.seq.gen.IGenerator;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.Map;

/**
 * 日期生成器
 *
 * @author Tanzj
 * @date 2023/7/16 22:14
 */
public class DateTimeGenerator implements IGenerator {

    private static final String TYPE = GeneratorTypeEnum.DATE_TIME.getValue();

    @Override
    public String getGeneratorType() {
        return TYPE;
    }

    @Override
    public String generate(String format, Map<String, String> parameterMap) {
        return DateFormatUtils.format(new Date(), format);
    }

}
