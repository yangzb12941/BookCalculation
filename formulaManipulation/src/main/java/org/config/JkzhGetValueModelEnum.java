package org.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum JkzhGetValueModelEnum {
    //参数解析模型：1-主动土压力计算、2-被动土压力计算、3-土压力零点计算、4-支撑轴力计算
    /**事件类型 0:回退事件 1:正常进行 2:拒绝/作废 **/
    主动土压力计算 (1,"主动土压力计算"),
    被动土压力计算(2,"被动土压力计算"),
    土压力零点所在土层(3,"土压力零点所在土层"),
    土压力零点深度计算(4,"土压力零点深度计算"),
    支撑轴力计算(5,"支撑轴力计算"),
    土压力系数计算(6,"土压力系数计算"),
    ;
    private Integer type;
    private String typeName;
}
