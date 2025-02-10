package com.tecpie.platform.seq.gen;

import com.google.common.collect.Maps;
import com.tecpie.platform.seq.gen.impl.DateTimeGenerator;
import com.tecpie.platform.seq.gen.impl.NumberGenerator;
import com.tecpie.platform.seq.gen.impl.StringGenerator;

import java.util.Map;

/**
 * 序列生成引擎
 *
 * @author Tanzj
 * @date 2022/3/17 22:06
 */
public class SequenceGenerateEngine {
    /**
     * 流水号生成引擎对象
     */
    private static SequenceGenerateEngine sequenceGenerateEngine;
    /**
     * 生成规则Map
     */
    private static Map<String, IGenerator> generatorMap;
    /**
     * 流水号子序列生成规则
     */
    private String[] subFormatStr;

    /**
     * 单例模式:
     */
    private SequenceGenerateEngine() {

    }

    public static void addGenerator(IGenerator generator) {
        generatorMap.put(generator.getGeneratorType(), generator);
    }

    public static SequenceGenerateEngine getInstance() {
        if (sequenceGenerateEngine == null) {
            sequenceGenerateEngine = new SequenceGenerateEngine();
            // 生成器
            generatorMap = Maps.newHashMap();
            addGenerator(new StringGenerator());
            addGenerator(new NumberGenerator());
            addGenerator(new DateTimeGenerator());
        }
        return sequenceGenerateEngine;
    }

    public void setFormatStr(String formatStr) {
        // 子序列分割符
        String splitChar = "#";
        // 流水号生成规则
        this.subFormatStr = formatStr.split(splitChar);
    }

    /**
     * 生成序列
     *
     * @param parameterMap 传递参数：key = currentNum代表要生成的数字、
     * @return String
     */
    public String generate(Map<String, String> parameterMap) {
        StringBuilder sb = new StringBuilder();
        for (String format : subFormatStr) {
            sb.append(this.generateSubSn(format, parameterMap));
        }
        return sb.toString();
    }

    /**
     * 生成子序列号
     *
     * @param format       子序列格式字符窜 ，如：Str@中央、NumSeq@5S0
     * @param parameterMap 参数Map
     * @return String
     */
    private String generateSubSn(String format, Map<String, String> parameterMap) {
        // 子序列内部分隔符
        String innerChar = "@";
        String[] innerSubStr = format.split(innerChar);
        if (innerSubStr.length < 2) {
            return "";
        }
        IGenerator generator = this.getGenerator(innerSubStr[0]);
        if (generator == null) {
            return "";
        } else {
            return generator.generate(innerSubStr[1], parameterMap);
        }
    }

    /**
     * 返回序列生成器
     *
     * @param type 序列类型
     * @return IGenerator
     */
    private IGenerator getGenerator(String type) {
        return generatorMap.get(type);
    }

    public static void main(String[] args) {
        String formatStr = "Str@Order#DateTime@yyyyMMdd#NumSeq@5S0";
        Map<String, String> parameterMap = Maps.newHashMap();
        parameterMap.put("currentNum", 10 + "");
        SequenceGenerateEngine sequenceGenerateEngine = SequenceGenerateEngine.getInstance();
        sequenceGenerateEngine.setFormatStr(formatStr);
        String gs = sequenceGenerateEngine.generate(parameterMap);
        System.out.println(gs);
    }

}
