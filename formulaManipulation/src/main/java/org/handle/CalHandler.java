package org.handle;

import com.googlecode.aviator.AviatorEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.aviatorPlugins.MathRadiansFunction;

@Slf4j
public class CalHandler implements IHandler{
    static {
        //装配角度转换函数
        AviatorEvaluator.addFunction(new MathRadiansFunction());
    }

    @Override
    public String execute(String fromula) {
        log.info("CalHandler befor:{}", fromula);
        Double zdCalRtUp = (Double) AviatorEvaluator.execute(fromula);
        String valueUp = String.format("%.2f", zdCalRtUp);
        log.info("CalHandler result:{}", valueUp);
        return valueUp;
    }

    @Override
    public void setParams(Object object) {
    }
}
