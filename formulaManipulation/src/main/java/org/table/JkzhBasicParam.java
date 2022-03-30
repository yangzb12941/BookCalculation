package org.table;

import lombok.Data;

/**
 * 基坑支护设计排桩法基本参数:
 * 1、各土层计算的参数 table
 * 2、地面堆载 Surcharge
 * 3、第一层支撑的轴线 Axis
 * 4、开挖深度 Depth
 */
@Data
public class JkzhBasicParam {

	//地面堆载
	private Double surcharge = 0.0;

	//第一层支撑的轴线
	private Double axis = 0.0;

	//开挖深度
	private Double depth = 0.0;

	//基坑外水位(主动土侧水位)
	private double zDWarterDepth = 0.0;

	//基内外水位(被动土侧水位)
	private double bDWarterDepth = 0.0;

	//水常量
	private double waterConstant = 0.0;

	//总土层数
	private int allLands = 0;

	//基坑外水位所在土层(主动土侧水位所在土层)
	private int zDWaterLand = 0;

	//基内外水位所在土层(被动土侧水位所在土层)
	private int bDWaterLand = 0;

	//开挖深度所在土层
	private int atDepthLand = 0;

	//土压力零点所在土层
	private int atZoneLand = 0;

	//土压力零点：这个参数时需要计算的
	private Double pressureZero = 0.0;

	//是否存在 主动土压力等于被动土压力
	private String zDEqualsBDKinds;
}
