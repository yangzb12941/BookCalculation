package org.calParam;

import lombok.Data;

/**
 * 基坑支护设计排桩法基本参数:
 * 1、各土层计算的参数 table
 * 2、地面堆载 Surcharge
 * 3、第一层支撑的轴线 Axis
 * 4、开挖深度 Depth
 */
@Data
public class JkzhBasicParam extends BasicParams{

	//当前土层需计算的参数
	private CalResult calResult;

	//第一层支撑的轴线
	private Double axis = 0.0;

	//开挖深度
	private Double depth = 0.0;

	//基坑外水位(主动土侧水位)
	private Double zDWarterDepth = 0.0;

	//基内外水位(被动土侧水位)
	private Double bDWarterDepth = 0.0;
}
