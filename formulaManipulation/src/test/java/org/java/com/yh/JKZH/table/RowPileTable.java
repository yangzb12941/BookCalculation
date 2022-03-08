package org.java.com.yh.JKZH.table;

public class RowPileTable {
	//土层参数计算依据表
	private String[][] tableSoilLayerParameters = {
		{"1", "杂填土","1.8","18.0","4.5","9"},
		{"2", "褐黄色粉质粘土","4.4","19.5","6.3","15"},
		{"3", "灰色淤泥质粉质粘土","3.1","20.0","9.2","17.7"},
		{"4", "灰色淤泥质粘土","4.8","19.5","9.8","15.6"},
		{"5", "灰色粉质粘土","5.5","19.7","12.8","18.6"},
	};
	public String[][] getTableSoilLayerParameters() {
		return tableSoilLayerParameters;
	}
}
