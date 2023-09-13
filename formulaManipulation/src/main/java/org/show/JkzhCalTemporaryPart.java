package org.show;

import lombok.Data;

import java.util.HashMap;

/**
 * 一个计算公式，需要才分成多个部分计算。则每一部分临时存储在这个结构里。
 * 例如：支撑处水平力计算
 * 是由(支撑轴力主动-支撑轴力被动)/(土压力零点-支撑的轴线)
 * 而 支撑轴力主动 和 支撑轴力被动 是需要拆分开，单独展开代入数值计算
 * 这时就需要使用JkzhCalTemporaryPart作为临时保存。
 */
@Data
public class JkzhCalTemporaryPart implements ILayout {
    private HashMap<String,String> layoutMap;
    private String[] layoutChar;

    public JkzhCalTemporaryPart(String[] layoutChar) {
        this.layoutMap = new HashMap<String, String>(layoutChar.length);
        this.layoutChar = layoutChar;
    }
}
