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

	//土层参数计算依据表
	private SoilQualityTable soilQualityTable;

	//地面堆载
	private Double Surcharge = 20.0;

	//第一层支撑的轴线
	private Double Axis = 0.4;

	//开挖深度
	private Double Depth = 5.0;

	//土压力零点：这个参数时需要计算的
	private Double pressureZero = 0.0;

	//土层参数计算依据表头
	private String[] soilQualityTableHeader = {"岩土层分布（从上至下）及分布特征序号", "土层名称","厚度(m)\nh","重度(kN/m3)\nγ","内聚力(kPa)\nc","内摩擦角(°)\nΨ"};

	//土压力系数头
	private String[] soilPressureTableHeader= {"序号", "土层名称","Ka","√Ka","Kp","√Kp"};

	//计算层次
	private int allLands;

	//开挖深度所在土层
	private int atLand;

	//土压力零点所在土层
	private int atZoneLand;

	public int getAllLands(){
		return soilQualityTable.getTable().length;
	}
}
