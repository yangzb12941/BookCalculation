package org.calParam;

import lombok.Data;

@Data
public class CalResult {
    //基坑外水位所在土层(主动土侧水位所在土层)
    private int zDWaterLand = 0;

    //基内外水位所在土层(被动土侧水位所在土层)
    private int bDWaterLand = 0;

    //开挖深度所在土层
    private int atDepthLand = 0;

    //土压力零点所在土层
    private int atZoneLand = 0;

    //土压力零点：这个参数时需要计算的
    private Double pressureZero = 0.0;

    //是否存在 主动土压力等于被动土压力
    private String zDEqualsBDKinds;

    //支撑位置所在土层
    private int axisAtLand = 0;

    //弯矩为零的点所在土层
    private int maxTcLand = 0;

    //弯矩为零的点深度，是距离土顶面的距离
    private Double maxTcDepth = 0d;
}
