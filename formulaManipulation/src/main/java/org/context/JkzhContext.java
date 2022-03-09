package org.context;

import lombok.Data;
import org.table.JkzhBasicParam;
import org.table.SoilPressureTable;

import java.util.HashMap;

/**
 * 基坑支护计算上下文对象
 */
@Data
public class JkzhContext extends IContext{
    //基坑支护计算 基本参数
    private JkzhBasicParam jkzhBasicParam;

    //基坑支护 土压力系数表
    private SoilPressureTable soilPressureTable;

    //计算过程的中间结果保存
    private HashMap<String,String> formate;
}
