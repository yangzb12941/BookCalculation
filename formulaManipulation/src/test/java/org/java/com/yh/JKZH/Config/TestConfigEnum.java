package org.java.com.yh.JKZH.Config;

/**
 *  排桩模板配置类
 *  p_{ak}=\sigma_{ak}K_{a,i}-2c_{i}\sqrt{K_{a,i}}
 *  K_{a,i}=\tan^{2}\left(45^{\circ}-\frac{\varphi_{i}}{2}\right)
 *  p_{pk}=\sigma_{pk}K_{p,i}+2c_{i}\sqrt{K_{p,i}}
 *  K_{p,i}=\tan^{2}\left(45^{\circ}+\frac{\varphi_{i}}{2}\right)
 *  \frac{H\times{\mathrm{P}^{\prime}}_{ak}}{\mathrm{P_{ak}}+{\mathrm{P}^{\prime}}_{ak}}\times\frac{1}{3}
 *  \frac{H\times{{\mathrm{P}^{\prime}}_{ak}}^2}{\mathrm{P_{ak}}+{\mathrm{P}^{\prime}}_{ak}}\times\frac{1}{2}
 *  \frac{2\times\mathrm{P_{ak}+}{\mathrm{P}^{\prime}}_{ak}}{\mathrm{P_{ak}}+{\mathrm{P}^{\prime}}_{ak}}\times\frac{H}{3}
 *  \left({\mathrm{P_{ak}}+{\mathrm{P}^{\prime}}_{ak}}\right)\times\frac{H}{2}
 */
public enum TestConfigEnum {
	Tp("Tp","\\frac{\\mathrm{N}}{\\varphi_{\\mathrm{x}} \\mathrm{A}}+\\frac{\\beta_{\\mathrm{mx}} \\mathrm{M}_{\\mathrm{x}}}{\\gamma_{\\mathrm{x}} \\mathrm{W}_{1 \\mathrm{x}}\\left(1-0.8 \\frac{\\mathrm{N}}{\\mathrm{N}_{\\mathrm{Ex}}^{\\prime}}\\right)}",
		"\\frac{123 \\times 10^{3}}{0.97 \\times 6353}+\\frac{1.0 \\times 196 \\times 10^{6}}{1.05 \\times 4.72 \\times 10^{6} \\times\\left(1-0.8 \\times \\frac{123}{2.9 \\times 10^{7}}\\right)}",
		"123*math.pow(10,3)/(0.97*6353)+1.0*196*math.pow(10,6)/(1.05*4.72*math.pow(10,6)*(1-0.8*123/(2.9*math.pow(10,7))))");

	private final String key;
	private final String latex;
	private final String calculate;
	private final String latexCal;

	TestConfigEnum(String key, String latex, String calculate, String latexCal) {
		this.key = key;
		this.latex = latex;
		this.calculate = calculate;
		this.latexCal = latexCal;
	}

	public String getKey() {
		return key;
	}

	public String getLatex() {
		return latex;
	}

	public String getCalculate() {
		return calculate;
	}

	public String getLatexCal() {
		return latexCal;
	}
}
