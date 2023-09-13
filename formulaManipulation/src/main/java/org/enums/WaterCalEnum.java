package org.enums;

public enum WaterCalEnum {

    水土分算("1",
            "水土分算"),
    水土合算("2",
            "水土合算"),;

    private final String key;//公式类型
    private final String value;//存原始公式展示

    WaterCalEnum(String key, String value) {
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
