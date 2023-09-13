package org.enums;

public enum StateMachineEnum {
    操作数(0,
            "操作数"),
    操作符(1,
            "操作符"),;

    private final int key;//公式类型
    private final String value;//存原始公式展示

    StateMachineEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
