package org.table;

import com.googlecode.aviator.AviatorEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.config.JkzhConfigEnum;
import org.config.JkzhGetValueModelEnum;
import org.context.JkzhContext;
import org.getValue.JkzhGetValues;
import org.handle.JkzhFromulaHandle;

/**
 * 基坑支护 土压力系数表。
 * 是通过 JkzhBasicParam中的table计算得出结果
 */
@Slf4j
public class SoilPressureTable implements ITable {
	private String[] head = {"序号", "土层名称","Ka","√Ka","Kp","√Kp"};
	//土层参数计算依据表
	private String[][] table;
	@Override
	public String[][] getTable() {
		return this.table;
	}

	public void setTable(String[][] table){
		this.table = table;
	}

	public SoilPressureTable(JkzhContext jkzhContext, JkzhFromulaHandle jkzhFromulaHandle){
		String[][] table = jkzhContext.getSoilQualityTable().getTable();
		String[][] tableParam = new String[table.length][table[0].length];
		tableParam[0] = head;
		JkzhGetValues jkzhGetValues = new JkzhGetValues();
		jkzhGetValues.setModel(JkzhGetValueModelEnum.土压力系数计算);
		for (int i = 1; i < table.length; i++) {
			tableParam[i][0] = table[i][0];
			tableParam[i][1] = table[i][1];
			String zdFillingCal = jkzhFromulaHandle.generalFromulaHandle(jkzhContext,jkzhFromulaHandle,i, JkzhConfigEnum.主动土压力系数.getCalculate(),jkzhGetValues);
			log.info("被动计算:{}",zdFillingCal);
			Double zdCalRt = (Double) AviatorEvaluator.execute(zdFillingCal);
			tableParam[i][2] = String.format("%.2f",zdCalRt);
			tableParam[i][3] = String.format("%.2f",Math.sqrt(zdCalRt));

			//处理计算结果的公式
			String bdFillingCal = jkzhFromulaHandle.generalFromulaHandle(jkzhContext,jkzhFromulaHandle,i,JkzhConfigEnum.被动土压力系数.getCalculate(),jkzhGetValues);
			log.info("被动计算:{}",bdFillingCal);
			Double bdCalRt = (Double) AviatorEvaluator.execute(bdFillingCal);
			tableParam[i][4] = String.format("%.2f",bdCalRt);
			tableParam[i][5] = String.format("%.2f",Math.sqrt(bdCalRt));
		}
		this.table = tableParam;
	}
}
