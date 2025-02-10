package com.tecpie.platform.seq.gen.impl;

import com.tecpie.platform.seq.gen.GeneratorTypeEnum;
import com.tecpie.platform.seq.gen.IGenerator;

import java.util.Map;

/**
 * 数值生成器
 *
 * @author Tanzj
 * @date 2021/8/25 14:30
 */
public class NumberGenerator implements IGenerator {

    private static final String TYPE = GeneratorTypeEnum.NUMBER.getValue();

    private static final String SPLIT = "S";

    @Override
    public String getGeneratorType() {
        return TYPE;
    }

    /**
     * 生成序列
     *
     * @param format       生成规则
     * @param parameterMap 参数Map
     * @return String
     */
    @Override
    public String generate(String format, Map<String, String> parameterMap) {
        String currentNum = parameterMap.get("currentNum");
        if (currentNum == null) {
            return "";
        }
        String[] charArr = format.split(SPLIT);
        int seqNumLength = Integer.parseInt(charArr[0]);
        char prefixChar = charArr[1].charAt(0);
        if (seqNumLength == 0) {
            return currentNum;
        } else {
            return appendPrefixChar(prefixChar, seqNumLength, currentNum);
        }
    }

    /**
     * 补足前缀以保证序列定长，如 0001, 保持 4 位，不足 4 位用'0'补齐
     *
     * @param prefixChar   需要补齐的前缀字符
     * @param seqNumLength 生成器需要返回的字符窜长度
     * @param currentNum   当前数值字符窜
     * @return String
     */
    private String appendPrefixChar(char prefixChar, int seqNumLength, String currentNum) {
        StringBuffer sb = new StringBuffer();
        for (int i = currentNum.length(); i < seqNumLength; i++) {
            sb.append(prefixChar);
        }
        sb.append(currentNum);
        return sb.toString();
    }

}
