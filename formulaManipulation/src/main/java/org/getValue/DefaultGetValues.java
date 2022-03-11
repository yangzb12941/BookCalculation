package org.getValue;
import org.entity.Param;

import java.util.List;

public abstract class DefaultGetValues implements GetValues{
    /**
     * 通过解析公式把每一个字符替换为对应的具体数值。值的顺序要与解析的字符一一匹配，
     * 若是错位，则会填充错误。
     * 例如：
     * (地面堆载+重度 \times 厚度+重度 \times 厚度+重度 \times 厚度+重度 \times 厚度+重度 \times 厚度) \times 主动土压力系数- 2 \times 内聚力 \times \sqrt{主动土压力系数}
     * 地面堆载、重度、厚度、主动土压力系数、内聚力
     * @param values
     * @return
     */
    @Override
    public String[] getValues(Object... values) {
        String[] result = null;
        //解析参数获取,给每个参数匹配值
        List<Param> params = getParam(values);
        result = matchValues(params,values);
        return result;
    }

    /**
     * 把(载堆面地_{5}+度重_{1} \times 度厚_{1}+度重_{2} \times 度厚_{2}+度重_{3} \times 度厚_{3})
     * 载堆面地_{5} 拆分成 Param.name = 载堆面地 、Param.index = 5
     * @param values
     * @return
     */
    public abstract List<Param> getParam(Object... values);

    public abstract String[] matchValues(List<Param> params,Object... values);
}
