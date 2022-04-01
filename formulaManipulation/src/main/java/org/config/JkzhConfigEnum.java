package org.config;

/**
 *  排桩模板配置类
 */
public enum JkzhConfigEnum {
	//[]：表明这是一个需要特殊处理的整体。
	//<?>:表明这是需要根据条件判断是否在公式中存在的部分。
	//<[?]>:表明这是需要根据条件判断是否在公式中存在的部分,且包含需要特殊处理的整体。
	//...:表名前面的整体需要展开。目前展开的场景是 土层压力计算需要展开
	//《...》 这里面是一个公式，才分成多个部分，其中某一部分又是一个需要单独处理的公式项
	// 适用于公式需要展开，但各个部分展开次数不一样。
	// 注意：calculate 和 latexCal 所有需要代入参数的都需要中文描述。
	//      因为这些中文字符在一类模板中是固定不变的。
    //<(1)>的1代表是否有底面堆载；2代表是否水土核算;③代表当前土层是否需要计算这一部分
	主动土压力("主动土压力",
			"\\sigma_{ak}K_{a,i}-2c_{i}\\sqrt{K_{a,i}}",
			"<③(<①地面堆载+>[重度*厚度+...]<②-([厚度+...]-基坑外水位)*水常量>)*主动土压力系数->2*黏聚力*根号主动土压力系数<②+([厚度+...]-基坑外水位)*水常量>",
			"<③(<①地面堆载+>[重度 \\times 厚度+...]<②-([厚度+...]-基坑外水位) \\times 水常量>) \\times 主动土压力系数->2 \\times 黏聚力 \\times 根号主动土压力系数<②+([厚度+...]-基坑外水位) \\times 水常量>"),

	被动土压力("被动土压力",
			"\\sigma_{pk}K_{p,i}+2c_{i}\\sqrt{K_{p,i}}",
			"<③([重度*厚度+...]<②-([厚度+...]-基坑外水位)*水常量>)*被动土压力系数+>2*黏聚力*根号被动土压力系数<②+([厚度+...]-基坑外水位)*水常量>",
			"<③([重度 \\times 厚度+...]<②-([厚度+...]-基坑外水位) \\times 水常量>) \\times 被动土压力系数+> 2 \\times 黏聚力 \\times 根号被动土压力系数<②+([厚度+...]-基坑外水位) \\times 水常量>"),

	主动土压力系数("主动土压力系数",
			"\\tan^{2}\\left(45^{\\circ}-\\frac{\\varphi_{i}}{2}\\right)",
			"math.pow(math.tan(math.toRadians(45-内摩擦角/2)),2)",
			"\\tan^{2}\\left(45^{\\circ}-\\frac{内摩擦角}{2}\\right)"),

	被动土压力系数("被动土压力系数",
			"\\tan^{2}\\left(45^{\\circ}+\\frac{\\varphi_{i}}{2}\\right)",
			"math.pow(math.tan(math.toRadians(45+内摩擦角/2)),2)",
			"\\tan^{2}\\left(45^{\\circ}+\\frac{内摩擦角}{2}\\right)"),

	土压力合力_主动上大于0_下大于0("土压力合力_主动上大于0_下大于0",
			"\\frac{{土压力强度顶面 + 土压力强度底面}}{2} \\times 厚度",
			"(主动土压力上 + 主动土压力下)/2*厚度",
			"\\frac{{主动土压力上 + 主动土压力下}}{2} \\times 厚度"),

	作用点位置_主动上大于0_下大于0("作用点位置_主动大于0_被动大于0",
			"\\frac{{2 \\times 土压力强度顶面 + 土压力强度底面}}{{土压力强度顶面 + 土压力强度底面}} \\times \\frac{厚度}{3}",
			"((2*主动土压力上 + 主动土压力下)/(主动土压力上 + 主动土压力下))*(厚度/3)",
			"\\frac{{2 \\times 主动土压力上 + 主动土压力下}}{{主动土压力上 + 主动土压力下}} \\times \\frac{厚度}{3}"),

	土压力合力_主动上小于0_下大于0("土压力合力_主动小于0_被动大于0",
			"\\frac{{{土压力强度底面^2} \\times 厚度}}{{2\\left(\\left|土压力强度顶面\\right| + 土压力强度底面\\right )}}",
			"math.pow(主动土压力上,2)*厚度/(2*(math.abs(主动土压力下)+主动土压力上))",
			"\\frac{{{主动土压力上^2} \\times 厚度}}{{2 \\times \\left(\\left|主动土压力下\\right| + 主动土压力上\\right)}}"),

	作用点位置_主动上小于0_下大于0("作用点位置_主动小于0_被动大于0",
			"\\frac{土压力强度底面}{{\\left|土压力强度顶面\\right| + 土压力强度底面}}\\times\\frac{{2 \\times 厚度}}{3}",
			"主动土压力下/(math.abs(主动土压力上)+主动土压力下)*((2*厚度)/3)",
			"\\frac{主动土压力下}{{\\left|主动土压力上\\right|+主动土压力下}}\\times\\frac{{2 \\times 厚度}}{3}"),

	土压力合力_主动上大于0_下小于0("土压力合力_主动上大于0_下小于0",
			"\\frac{{{土压力强度顶面^2} \\times 厚度}}{{2\\left(\\left|土压力强度底面\\right| + 土压力强度顶面\\right )}}",
			"math.pow(主动土压力上,2)*厚度/(2*(math.abs(主动土压力下)+主动土压力上))",
			"\\frac{{{主动土压力上^2} \\times 厚度}}{{2 \\times \\left(\\left|主动土压力下\\right| + 主动土压力上\\right)}}"),

	作用点位置_主动上大于0_下小于0("作用点位置_主动上大于0_下小于0",
			"厚度-\\frac{土压力强度顶面}{土压力强度顶面+\\left|土压力强度底面\\right|}\\times \\frac{2}{3}",
			"厚度-主动土压力上/(主动土压力上+math.abs(主动土压力下))*2/3",
			"厚度-\\frac{主动土压力上}{主动土压力上+\\left|主动土压力下\\right|}\\times \\frac{2}{3}"),

	土压力合力_被动上大于0_下大于0("土压力合力_主动上大于0_下大于0",
			"\\frac{{土压力强度顶面 + 土压力强度底面}}{2} \\times 厚度",
			"(被动土压力上 + 被动土压力下)/2*厚度",
			"\\frac{{被动土压力上 + 被动土压力下}}{2} \\times 厚度"),

	作用点位置_被动上大于0_下大于0("作用点位置_被动上大于0_下大于0",
			"\\frac{{2 \\times 土压力强度顶面 + 土压力强度底面}}{{土压力强度顶面 + 土压力强度底面}} \\times \\frac{{厚度}}{3}",
			"((2*被动土压力上 + 被动土压力下)/(被动土压力上 + 被动土压力下))*(厚度/3)",
			"\\frac{{2 \\times 被动土压力上 + 被动土压力下}}{{被动土压力上 + 被动土压力下}} \\times \\frac{{厚度}}{3}"),

	土压力合力_被动上小于0_下大于0("土压力合力_被动上小于0_下大于0",
			"\\frac{{{土压力强度底面^2} \\times 厚度}}{{2\\left(\\left|土压力强度顶面\\right| + 土压力强度底面\\right )}}",
			"math.pow(被动土压力上,2)*厚度/(2*(math.abs(被动土压力下)+被动土压力上))",
			"\\frac{{{被动土压力上^2} \\times 厚度}}{{2 \\times \\left(\\left|被动土压力下\\right| + 被动土压力上\\right)}}"),

	作用点位置_被动上小于0_下大于0("作用点位置_被动上小于0_下大于0",
			"\\frac{土压力强度底面}{{\\left|土压力强度顶面\\right| + 土压力强度底面}}\\times\\frac{{2 \\times 厚度}}{3}",
			"被动土压力下/(math.abs(被动土压力上)+主动土压力下)*((2*厚度)/3)",
			"\\frac{被动土压力下}{{\\left|被动土压力上\\right|+被动土压力下}}\\times\\frac{{2 \\times 厚度}}{3}"),

	土压力合力_被动上大于0_下小于0("土压力合力_被动上大于0_下小于0",
			"\\frac{{{土压力强度顶面^2} \\times 厚度}}{{2\\left(\\left|土压力强度底面\\right| + 土压力强度顶面\\right )}}",
			"math.pow(被动土压力上,2)*厚度/(2*(math.abs(被动土压力下)+被动土压力上))",
			"\\frac{{{被动土压力上^2} \\times 厚度}}{{2 \\times \\left(\\left|被动土压力下\\right| + 被动土压力上\\right)}}"),

	作用点位置_被动上大于0_下小于0("作用点位置_被动上大于0_下小于0",
			"厚度-\\frac{土压力强度顶面}{土压力强度顶面+\\left|土压力强度底面\\right|}\\times \\frac{2}{3}",
			"厚度-被动土压力上/(被动土压力上+math.abs(被动土压力下))*2/3",
			"厚度-\\frac{被动土压力上}{被动土压力上+\\left|被动土压力下\\right|}\\times \\frac{2}{3}"),

	支撑轴力("支撑轴力",
			"\\frac{{{h}_{an}}\\sum{{{E}_{ac}}-{{h}_{pn}}\\sum{{{E}_{pc}}}}}{{{h}_{Tn}}+{{h}_{cn}}}",
			"((支撑轴力主动-支撑轴力被动)<④-[支撑轴力*支撑轴臂-...]>)/(土压力零点-支撑的轴线)",
			"\\frac{(支撑轴力主动-支撑轴力被动)<④-[支撑轴力  \\times 支撑轴臂-...]>}{(土压力零点-支撑的轴线)}"),

	支撑轴力主动("支撑轴力主动",
			"",
			"[主动土压力合力*主动土作用点位置+...]",
			"[主动土压力合力 \\times 主动土作用点位置+...]"),

	支撑轴力被动("支撑轴力被动",
			"",
			"[被动土压力合力*被动土作用点位置+...]",
			"[被动土压力合力 \\times 被动土作用点位置+...]"),;

	private final String key;//公式类型
	private final String latex;//存原始公式展示
	private final String calculate;//代入具体计算参数计算结果的公式，不需要在文档展示
	private final String latexCal;//代入具体参数的需要在文档中展示的公式

	JkzhConfigEnum(String key, String latex, String calculate, String latexCal) {
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
