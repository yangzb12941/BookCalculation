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
        this.layoutChar = new String[]{
                "被动合力至反弯点的距离",
                "主动合力至反弯点的距离",
                "各层土的被动合力",
                "各层土的主动合力",
                "支护结构外侧应力",
                "支护结构内侧应力",
                "主动土压力系数",
                "被动土压力系数",
                "支护结构外侧",
                "支护结构内侧",
                "轴向支反力"};

        this.layoutMap = new HashMap<String, String>(20);
        //被动合力至反弯点的距离
        this.layoutMap.put("被动合力至反弯点的距离","h_{pn}");

        //主动合力至反弯点的距离
        this.layoutMap.put("主动合力至反弯点的距离","h_{an}");

        //各层土的被动合力
        this.layoutMap.put("各层土的被动合力","\\sum{{{E}_{pc}}}");

        //各层土的主动合力
        this.layoutMap.put("各层土的主动合力","\\sum{{{E}_{ac}}}");

        //支护结构外侧应力
        this.layoutMap.put("支护结构外侧应力","{\\sigma}_{ak}");

        //支护结构内侧应力
        this.layoutMap.put("支护结构内侧应力","{\\sigma}_{pk}");

        //主动土压力系数
        this.layoutMap.put("主动土压力系数","K_{a,i}");

        //被动土压力系数
        this.layoutMap.put("被动土压力系数","K_{p,i}");

        //支护结构外侧
        this.layoutMap.put("支护结构外侧","P_{ak}");

        //支护结构内侧
        this.layoutMap.put("支护结构内侧","P_{pk}");

        //轴向支反力
        this.layoutMap.put("轴向支反力","T_{cn}");

        /**************公式前缀部分**************/
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
    }
}
