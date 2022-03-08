package org.aviatorPlugins;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

public class MathRadiansFunction extends AbstractFunction {

	private static final long serialVersionUID = -4156690146211829531L;


	@Override
	public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
		Number num = FunctionUtils.getNumberValue(arg1, env);
		return new AviatorDouble(Math.toRadians(num.doubleValue()));

	}

	@Override
	public String getName() {
		return "math.toRadians";
	}
}
