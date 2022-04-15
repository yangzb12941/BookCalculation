package org.enums;

public enum ConditionEnum {

    地面堆载("地面堆载",
            "①"),
    水土分算("水土分算",
            "②"),
    首层土计算("首层土计算",
            "③"),
    多工况支撑计算("多工况支撑计算",
            "④"),
    最大弯矩位置("最大弯矩位置",
            "⑤"),
    最大弯矩值("最大弯矩值",
            "⑥"),;

    private final String key;//公式类型
    private final String value;//存原始公式展示

    ConditionEnum(String key, String value) {
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
