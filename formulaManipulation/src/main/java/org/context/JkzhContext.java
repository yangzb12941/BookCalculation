package org.context;

import lombok.Data;
import org.table.JkzhBasicParam;
import org.table.SoilPressureTable;

/**
 * 基坑支护计算上下文对象
 */
@Data
public class JkzhContext extends IContext{
    //基坑支护计算 基本参数
    private JkzhBasicParam jkzhBasicParam;

    //基坑支护 土压力系数表
    private SoilPressureTable soilPressureTable;
}
