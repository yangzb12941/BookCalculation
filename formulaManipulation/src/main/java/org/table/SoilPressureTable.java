package org.table;

import lombok.extern.slf4j.Slf4j;
import org.config.JkzhConfigEnum;
import org.config.JkzhGetValueModelEnum;
import org.constant.Constant;
import org.context.JkzhContext;
import org.entity.NoExpansionParam;
import org.fromulaEntity.FromulaEntity;
import org.getValue.JkzhGetValues;
import org.handle.AppendSubscriptHandler;
import org.handle.CalHandler;
import org.handle.FillValueHandler;
import org.handle.NoExpansionHandler;

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

	public SoilPressureTable(JkzhContext jkzhContext){
		String[][] table = jkzhContext.getSoilQualityTable().getTable();
		String[][] tableParam = new String[table.length][table[0].length-1];
		tableParam[0] = head;
		JkzhGetValues jkzhGetValues = new JkzhGetValues(JkzhGetValueModelEnum.土压力系数计算,jkzhContext);
		//主动土系数计算
		FromulaEntity zdFromulaEntity = new FromulaEntity(JkzhConfigEnum.主动土压力系数.getCalculate());
		zdFromulaEntity
				.addHandler((new AppendSubscriptHandler()).setParams(Constant.FlagString))
				.addHandler((new NoExpansionHandler()).setParams(new NoExpansionParam(0)))
				.addHandler((new FillValueHandler()).setParams(jkzhGetValues))
		        .addHandler(new CalHandler());

		//被动土系数计算
		FromulaEntity bdFromulaEntity = new FromulaEntity(JkzhConfigEnum.被动土压力系数.getCalculate());
		bdFromulaEntity
				.addHandler((new AppendSubscriptHandler()).setParams(Constant.FlagString))
				.addHandler((new NoExpansionHandler()).setParams(new NoExpansionParam(0)))
				.addHandler((new FillValueHandler()).setParams(jkzhGetValues))
		        .addHandler(new CalHandler());

		for (int i = 1; i < table.length; i++) {
			tableParam[i][0] = table[i][0];
			tableParam[i][1] = table[i][1];
			NoExpansionHandler zdHandler = (NoExpansionHandler)zdFromulaEntity.getHandler(NoExpansionHandler.class);
			zdHandler.getNoExpansionParam().setCurFloor(i);
			String zdFillingCal = zdFromulaEntity.compile();
			log.info("被动计算:{}",zdFillingCal);
			tableParam[i][2] = zdFillingCal;
			tableParam[i][3] = String.format("%.2f",Math.sqrt(Double.valueOf(zdFillingCal)));

			//处理计算结果的公式
			NoExpansionHandler bdHandler = (NoExpansionHandler)bdFromulaEntity.getHandler(NoExpansionHandler.class);
			bdHandler.getNoExpansionParam().setCurFloor(i);
			String bdFillingCal = bdFromulaEntity.compile();
			log.info("被动计算:{}",bdFillingCal);
			tableParam[i][4] =bdFillingCal;
			tableParam[i][5] = String.format("%.2f",Math.sqrt(Double.valueOf(bdFillingCal)));
		}
		this.table = tableParam;
	}
}
