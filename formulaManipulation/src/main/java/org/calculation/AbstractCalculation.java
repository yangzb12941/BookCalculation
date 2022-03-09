package org.calculation;

import com.googlecode.aviator.AviatorEvaluator;
import org.aviatorPlugins.MathRadiansFunction;

public abstract class AbstractCalculation implements ICalculation{
    static {
        //装配角度转换函数
        AviatorEvaluator.addFunction(new MathRadiansFunction());
    }
}
