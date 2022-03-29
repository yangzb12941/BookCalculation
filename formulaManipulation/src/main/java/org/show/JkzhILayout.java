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
        this.layoutChar = new String[]{
                "基坑底面至反弯点的距离",
                "主动合力至反弯点的距离",
                "被动合力至反弯点的距离",
                "主动深度到水位的距离",
                "支点至基坑底面的距离",
                "根号主动土压力系数",
                "根号被动土压力系数",
                "各层土的主动合力",
                "各层土的被动合力",
                "主动土压力系数",
                "被动土压力系数",
                "土压力强度顶面",
                "土压力强度底面",
                "支撑轴力主动",
                "支撑轴力被动",
                "主动土压力上",
                "主动土压力下",
                "被动土压力上",
                "被动土压力下",
                "地面堆载",
                "内摩擦角",
                "土层厚度",
                "支撑轴力",
                "粘聚力",
                "水常量",
                "重度",
                "厚度"};

        this.layoutMap = new HashMap<String, String>(20);
        //主动土压力上
        this.layoutMap.put("主动土压力上","P_{akn}");

        //主动土压力下
        this.layoutMap.put("主动土压力下","{\\mathrm{P}^{\\prime}}_{akn}");

        //被动土压力上
        this.layoutMap.put("被动土压力上","P_{pkn}");

        //被动土压力下
        this.layoutMap.put("被动土压力下","{\\mathrm{P}^{\\prime}}_{pkn}");

        //地面堆载
        this.layoutMap.put("地面堆载","q");

        //重度
        this.layoutMap.put("重度","\\gamma_{i}");

        //厚度
        this.layoutMap.put("厚度","h_{n}");

        //粘聚力
        this.layoutMap.put("粘聚力","C_{i}");

        //内摩擦角
        this.layoutMap.put("内摩擦角","\\varphi_{i}");

        //根号主动土压力系数
        this.layoutMap.put("根号主动土压力系数","Ka_{n}");

        //根号被动土压力系数
        this.layoutMap.put("根号被动土压力系数","Kp_{n}");

        //主动土压力系数
        this.layoutMap.put("主动土压力系数","Ka_{n}");

        //被动土压力系数
        this.layoutMap.put("被动土压力系数","Kp_{n}");

        //各层土的主动合力
        this.layoutMap.put("各层土的主动合力","E_{ac}");

        //主动合力至反弯点的距离
        this.layoutMap.put("主动合力至反弯点的距离","h_{an}");

        //各层土的被动合力
        this.layoutMap.put("各层土的被动合力","E_{pc}");

        //被动合力至反弯点的距离
        this.layoutMap.put("被动合力至反弯点的距离","h_{pn}");

        //支撑轴力
        this.layoutMap.put("支撑轴力","T_{cn}");

        //支点至基坑底面的距离
        this.layoutMap.put("支点至基坑底面的距离","{h}_{Tn}");

        //基坑底面至反弯点的距离
        this.layoutMap.put("基坑底面至反弯点的距离","{h}_{cn}");

        //土压力强度顶面
        this.layoutMap.put("土压力强度顶面","a");

        //土压力强度底面
        this.layoutMap.put("土压力强度底面","b");

        //土层厚度
        this.layoutMap.put("土层厚度","h");

        //主动深度到水位的距离
        this.layoutMap.put("主动深度到水位的距离","h_{wn}");

        //水常量
        this.layoutMap.put("水常量","10");
    }
}
