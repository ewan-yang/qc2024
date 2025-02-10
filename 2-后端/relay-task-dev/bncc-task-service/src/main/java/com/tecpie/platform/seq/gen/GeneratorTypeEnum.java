package com.tecpie.platform.seq.gen;

/**
 * 序列生成枚举类
 *
 * @author Tanzj
 * @date 2023/7/16 22:10
 */
public enum GeneratorTypeEnum {
    /**
     * 支持的类型
     */
    STRING("Str", "字符窜类型"),
    DATE_TIME("DateTime", "日期类型"),
    NUMBER("NumSeq", "数字类型");

    private String value;

    private String desc;

    GeneratorTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

}
