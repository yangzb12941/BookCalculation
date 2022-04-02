package org.enums;

public enum CalculateSectionEnum {
    上顶面("上顶面",
            "1"),
    下底面("下底面",
            "2"),;

    private final String key;//公式类型
    private final String value;//存原始公式展示

    CalculateSectionEnum(String key, String value) {
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
