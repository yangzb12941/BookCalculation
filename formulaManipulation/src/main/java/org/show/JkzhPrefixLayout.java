package org.show;

import lombok.Data;

import java.util.HashMap;

/**
 * 主要是用于公式前缀和单独的字符显示
 */
@Data
public class JkzhPrefixLayout implements ILayout{
    private HashMap<String,String> layoutMap;
    private String[] layoutChar;

    public JkzhPrefixLayout() {
        this.layoutChar = null;

        this.layoutMap = new HashMap<String, String>(39);

        //主动土压力上
        this.layoutMap.put("主动土压力上","P_{akn}");

        //主动土压力下
        this.layoutMap.put("主动土压力下","{\\mathrm{P}^{\\prime}}_{akn}");

        //被动土压力上
        this.layoutMap.put("被动土压力上","P_{pkn}");

        //被动土压力下
        this.layoutMap.put("被动土压力下","{\\mathrm{P}^{\\prime}}_{pkn}");

        //主动土压力计算公式
        this.layoutMap.put("主动土压力计算公式","P_{ak}");

        //主动土压力系数计算公式
        this.layoutMap.put("主动土压力系数计算公式","K_{a,i}");

        //被动土压力计算公式
        this.layoutMap.put("被动土压力计算公式","P_{pk}");

        //被动土压力系数计算公式
        this.layoutMap.put("被动土压力系数计算公式","K_{p,i}");

        //支反力计算公式
        this.layoutMap.put("支反力计算公式","T_{cn}");

        //黏聚力
        this.layoutMap.put("黏聚力","C_{i}");

        //内摩擦角
        this.layoutMap.put("内摩擦角","\\varphi_{i}");

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

        //支护结构外侧
        this.layoutMap.put("支护结构外侧","P_{ak}");

        //支护结构内侧
        this.layoutMap.put("支护结构内侧","P_{pk}");

        //支护结构外侧应力
        this.layoutMap.put("支护结构外侧应力","{\\sigma}_{ak}");

        //支护结构内侧应力
        this.layoutMap.put("支护结构内侧应力","{\\sigma}_{pk}");

        //主动土压力系数
        this.layoutMap.put("主动土压力系数","K_{a,i}");

        //被动土压力系数
        this.layoutMap.put("被动土压力系数","K_{p,i}");

        //被动合力至反弯点的距离
        this.layoutMap.put("被动合力至反弯点的距离","h_{pn}");

        //各层土的被动合力
        this.layoutMap.put("各层土的被动合力","\\sum{{E}_{pc}}");

        //轴向支反力
        this.layoutMap.put("轴向支反力","T_{cn}");

        //主动合力至反弯点的距离
        this.layoutMap.put("主动合力至反弯点的距离","h_{an}");

        //各层土的主动合力
        this.layoutMap.put("各层土的主动合力","\\sum{{E}_{ac}}");

        //主动减被动顶
        this.layoutMap.put("主动减被动顶","\\Delta{e_{n}}");

        //主动减被动底
        this.layoutMap.put("主动减被动底","\\Delta{\\mathrm{e}^{\\prime}}_{n}");

        //主动土压力合力计算公式
        this.layoutMap.put("主动土压力合力计算公式","E_{a}");

        //主动土压力合力计算
        this.layoutMap.put("主动土压力合力计算","E_{an}");

        //主动作用点位置计算公式
        this.layoutMap.put("主动作用点位置计算公式","h_{a}");

        //主动作用点位置计算
        this.layoutMap.put("主动作用点位置计算","h_{an}");

        //被动土压力合力计算公式
        this.layoutMap.put("被动土压力合力计算公式","E_{p}");

        //被动土压力合力计算
        this.layoutMap.put("被动土压力合力计算","E_{pn}");

        //被动作用点位置计算公式
        this.layoutMap.put("被动作用点位置计算公式","h_{p}");

        //被动作用点位置计算
        this.layoutMap.put("被动作用点位置计算","h_{pn}");

        //支点反力计算
        this.layoutMap.put("支点反力计算","T_{cn}");
    }
}
