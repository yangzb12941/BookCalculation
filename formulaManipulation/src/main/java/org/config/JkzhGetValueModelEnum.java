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
    主动土压力零点深度计算(4,"主动土压力零点深度计算"),
    被动土压力零点深度计算(5,"被动土压力零点深度计算"),
    主动支撑轴力计算(6,"主动支撑轴力计算"),
    被动支撑轴力计算(7,"被动支撑轴力计算"),
    土压力系数计算(87,"土压力系数计算"),
    主动土压力合力计算(9,"主动土压力合力计算"),
    主动作用点位置(10,"主动作用点位置"),
    被动土压力合力计算(11,"被动土压力合力计算"),
    被动作用点位置(12,"被动作用点位置"),
    支撑轴力计算(13,"支撑轴力计算"),
    ;
    private Integer type;
    private String typeName;
}
