package org.show;

import lombok.Data;

import java.util.HashMap;

/**
 * 基坑支护布局展示。
 * 例如：\mathrm{P_{ak}} 替换为其他字符显示 \mathrm{e_{a上}}。
 */
@Data
public class JkzhILayout implements ILayout {
    private HashMap<String,String> layoutMap;
    private String[] layoutChar;

    public JkzhILayout() {
        this.layoutChar = new String[]{"地面堆载", "重度", "厚度", "内聚力", "内摩擦角", "主动土压力系数",
                "被动土压力系数","根号主动土压力系数","根号被动土压力系数",
                "主动土压力合力", "主动土作用点位置", "被动土压力合力",
                "被动土作用点位置", "主动土层至土压力零点距离","被动土层至土压力零点距离", "土压力零点", "支撑的轴线",
                "主动土压力","被动土压力"};

        this.layoutMap = new HashMap<String, String>(32);

        //地面堆载
        this.layoutMap.put("地面堆载","q");

        //重度
        this.layoutMap.put("重度","\\gamma_{n}");

        //厚度
        this.layoutMap.put("厚度","h_{n}");

        //内聚力
        this.layoutMap.put("内聚力","C_{n}");

        //内摩擦角
        this.layoutMap.put("内摩擦角","\\varphi_{n}");

        //主动土压力系数
        this.layoutMap.put("主动土压力系数","Ka_{n}");

        //被动土压力系数
        this.layoutMap.put("被动土压力系数","Kp_{n}");

        //主动土压力系数
        this.layoutMap.put("根号主动土压力系数","Ka_{n}");

        //被动土压力系数
        this.layoutMap.put("根号被动土压力系数","Kp_{n}");

        //主动土压力合力
        this.layoutMap.put("主动土压力合力","E_{an}");

        //主动土作用点位置
        this.layoutMap.put("主动土作用点位置","h_{an}");

        //被动土压力合力
        this.layoutMap.put("被动土压力合力","E_{pn}");

        //被动土作用点位置
        this.layoutMap.put("被动土作用点位置","h_{pn}");

        //主动土层至土压力零点距离
        this.layoutMap.put("主动土层至土压力零点距离","L_{an}");

        //被动土层至土压力零点距离
        this.layoutMap.put("被动土层至土压力零点距离","L_{pn}");

        //土压力零点
        this.layoutMap.put("土压力零点","O");

        //支撑的轴线
        this.layoutMap.put("支撑的轴线","Z");

        //***********这几个不需要替换，只需要在输出公式的时候使用**********
        //主动土压力
        this.layoutMap.put("主动土压力","P_{akn}");

        //主动土压力下层
        this.layoutMap.put("主动土压力底层","{\\mathrm{P}^{\\prime}}_{akn}");

        //被动土压力
        this.layoutMap.put("被动土压力","P_{pkn}");

        //被动土压力下层
        this.layoutMap.put("被动土压力底层","{\\mathrm{P}^{\\prime}}_{pkn}");
    }
}
