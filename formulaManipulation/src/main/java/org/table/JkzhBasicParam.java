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
	private Double surcharge = 20.0;

	//第一层支撑的轴线
	private Double axis = 0.4;

	//开挖深度
	private Double depth =7.0;

	//土压力零点：这个参数时需要计算的
	private Double pressureZero = 0.0;

	//计算层次
	private int allLands = 4;

	//开挖深度所在土层
	private int atDepthLand;

	//土压力零点所在土层
	private int atZoneLand;

	//是否存在 主动土压力等于被动土压力
	private String zDEqualsBDKinds;
}
