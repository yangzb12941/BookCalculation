package org.show;

import lombok.Data;

import java.util.HashMap;

/**
 * 主要是用于公式中文字符替换成对应字符展示
 */
@Data
public class JkzhFormulaLayout implements ILayout{
    private HashMap<String,String> layoutMap;
    private String[] layoutChar;

    public JkzhFormulaLayout() {
        this.layoutChar = new String[]{
                "土压力强度底面",
                "土压力强度顶面",
                "厚度"};

        this.layoutMap = new HashMap<String, String>(9);

        //土压力强度底面
        this.layoutMap.put("土压力强度底面","b");

        //土压力强度顶面
        this.layoutMap.put("土压力强度顶面","a");

        //厚度
        this.layoutMap.put("厚度","h_{n}");
    }
}
