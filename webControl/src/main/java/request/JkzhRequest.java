package request;

import lombok.Data;

@Data
public class JkzhRequest {

    //地面堆载
    private Double surcharge = 20.0;

    //第一层支撑的轴线
    private Double axis = 0.4;

    //开挖深度
    private Double depth =7.0;

    //基坑外水位(主动土侧水位)
    private double zDWarterDepth = 2.5;

    //基内外水位(被动土侧水位)
    private double bDWarterDepth = 10.5;

    //水常量
    private double waterConstant = 20.0;

    //土层参数表
    private String[][] table;
}
