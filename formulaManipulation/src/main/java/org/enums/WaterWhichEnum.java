package org.enums;

public enum WaterWhichEnum {

    主动侧水位("1",
            "主动侧水位"),
    被动侧水位("2",
            "被动侧水位"),;

    private final String key;//公式类型
    private final String value;//存原始公式展示

    WaterWhichEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
