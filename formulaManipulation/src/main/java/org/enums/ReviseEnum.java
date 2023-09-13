package org.enums;

public enum ReviseEnum {

    公式修正("1",
            "公式修正"),;

    private final String key;//公式类型
    private final String value;//存原始公式展示

    ReviseEnum(String key, String value) {
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
