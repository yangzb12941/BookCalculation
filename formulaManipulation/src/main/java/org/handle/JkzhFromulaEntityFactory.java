package org.handle;

import lombok.extern.slf4j.Slf4j;
import org.config.JkzhConfigEnum;
import org.constant.Constant;
import org.entity.ExpansionParam;
import org.enums.ReviseEnum;
import org.enums.WaterWhichEnum;
import org.fromulaEntity.FromulaEntity;
import org.getValue.JkzhGetValues;
import org.handleParams.FirstFloorHandlerParam;
import org.handleParams.WaterHandlerParams;
import org.handler.*;
import org.show.ILayout;

@Slf4j
public class JkzhFromulaEntityFactory {

	private static volatile JkzhFromulaEntityFactory jkzhFromulaEntityFactory;

	private JkzhFromulaEntityFactory(){}

	public static JkzhFromulaEntityFactory getJkzhFromulaEntityFactory() {
		if(null == jkzhFromulaEntityFactory){
			synchronized (JkzhFromulaEntityFactory.class) {
				if(null == jkzhFromulaEntityFactory){
					jkzhFromulaEntityFactory = new JkzhFromulaEntityFactory();
				}
			}
		}
		return jkzhFromulaEntityFactory;
	}


	/**
	 * 土压力计算结果：包括如下处理器
	 * 1、添加首层土判断处理器
	 * 2、添加地面堆载处理器
	 * 3、添加水土分算处理器
	 * 4、添加元素标记处理器
	 * 5、公式修正处理器
	 * 6、添加展开公式处理器
	 * 7、添加值填充处理器
	 * 8、添加值填充处理器
	 * 9、计算过计算处理器
	 * @param jkzhGetValues
	 * @param jkzhConfigEnum
	 * @param waterWhichEnum
	 * @return
	 */
	public FromulaEntity soilPressureToCal(int time,
										   int beginFloor,
										   int endFloor,
			                               JkzhGetValues jkzhGetValues,
										   JkzhConfigEnum jkzhConfigEnum,
										   WaterWhichEnum waterWhichEnum){
		//用于计算结果
		FromulaEntity calFromulaEntity = new FromulaEntity(jkzhConfigEnum.getCalculate());
		calFromulaEntity
				//添加首层土判断处理器
				.addHandler(new FirstFloorHandler().setParams(new FirstFloorHandlerParam(
						jkzhGetValues.getJkzhContext().getJkzhBasicParam(),
						time+beginFloor,
						beginFloor,
						jkzhConfigEnum)))
				//添加地面堆载处理器
				.addHandler(new SurchargeHandler().setParams(jkzhGetValues.getJkzhContext().getJkzhBasicParam()))
				//添加水土分算处理器
				.addHandler(new WaterHandler().setParams(new WaterHandlerParams(jkzhGetValues.getJkzhContext().getSoilQualityTable(),jkzhGetValues.getJkzhContext().getJkzhBasicParam(), waterWhichEnum)))
				//添加元素标记处理器
				.addHandler(new AppendSubscriptHandler().setParams(Constant.FlagString))
				//添加展开公式处理器
				.addHandler(new ExpansionHandler().setParams(new ExpansionParam(time,beginFloor,endFloor)))
				//公式修正处理器
				.addHandler(new ReviseHandler().setParams(ReviseEnum.公式修正))
				//添加值填充处理器
				.addHandler(new FillValueHandler().setParams(jkzhGetValues))
				//添加值填充处理器
				.addHandler(new CalHandler());
		return calFromulaEntity;
	}

	/**
	 * 土压力计算结果：包括如下处理器
	 * 1、添加首层土判断处理器
	 * 2、添加地面堆载处理器
	 * 3、添加水土分算处理器
	 * 4、添加元素标记处理器
	 * 5、公式修正处理器
	 * 6、添加展开公式处理器
	 * 7、添加值填充处理器
	 * 8、添加值填充处理器
	 * 9、计算过计算处理器
	 * @param jkzhGetValues
	 * @param jkzhConfigEnum
	 * @param waterWhichEnum
	 * @return
	 */
	public FromulaEntity calSolveEquations(int time,
										   int beginFloor,
										   int endFloor,
										   JkzhGetValues jkzhGetValues,
										   JkzhConfigEnum jkzhConfigEnum,
										   WaterWhichEnum waterWhichEnum){
		//用于计算结果
		FromulaEntity calFromulaEntity = new FromulaEntity(jkzhConfigEnum.getCalculate());
		calFromulaEntity
				//添加首层土判断处理器
				.addHandler(new FirstFloorHandler().setParams(new FirstFloorHandlerParam(
						jkzhGetValues.getJkzhContext().getJkzhBasicParam(),
						time+beginFloor,
						beginFloor,
						jkzhConfigEnum)))
				//添加地面堆载处理器
				.addHandler(new SurchargeHandler().setParams(jkzhGetValues.getJkzhContext().getJkzhBasicParam()))
				//添加水土分算处理器
				.addHandler(new WaterHandler().setParams(new WaterHandlerParams(jkzhGetValues.getJkzhContext().getSoilQualityTable(),jkzhGetValues.getJkzhContext().getJkzhBasicParam(), waterWhichEnum)))
				//添加元素标记处理器
				.addHandler(new AppendSubscriptHandler().setParams(Constant.FlagString))
				//添加展开公式处理器
				.addHandler(new ExpansionHandler().setParams(new ExpansionParam(time,beginFloor,endFloor)))
				//公式修正处理器
				.addHandler(new ReviseHandler().setParams(ReviseEnum.公式修正))
				//添加值填充处理器
				.addHandler(new FillValueHandler().setParams(jkzhGetValues));
		return calFromulaEntity;
	}

	/**
	 * 土压力计算公式：包括如下处理器
	 * 1、添加首层土判断处理器
	 * 2、添加地面堆载处理器
	 * 3、添加水土分算处理器
	 * 4、添加元素标记处理器
	 * 5、公式修正处理器
	 * 6、添加展开公式处理器
	 * 7、添加值填充处理器
	 * 8、添加值填充处理器
	 * @param jkzhGetValues
	 * @param jkzhConfigEnum
	 * @param waterWhichEnum
	 * @return
	 */
	public FromulaEntity soilPressureToLatex(int time,
											 int beginFloor,
											 int endFloor,
			                                 JkzhGetValues jkzhGetValues,
											 JkzhConfigEnum jkzhConfigEnum,
											 WaterWhichEnum waterWhichEnum){
		//用于word展示
		FromulaEntity latexFromulaEntity = new FromulaEntity(jkzhConfigEnum.getLatexCal());
		latexFromulaEntity
				//添加首层土判断处理器
				.addHandler(new FirstFloorHandler().setParams(new FirstFloorHandlerParam(
						jkzhGetValues.getJkzhContext().getJkzhBasicParam(),
						time+beginFloor,
						beginFloor,
						jkzhConfigEnum)))
				//添加地面堆载处理器
				.addHandler(new SurchargeHandler().setParams(jkzhGetValues.getJkzhContext().getJkzhBasicParam()))
				//添加水土分算处理器
				.addHandler(new WaterHandler().setParams(new WaterHandlerParams(jkzhGetValues.getJkzhContext().getSoilQualityTable(),jkzhGetValues.getJkzhContext().getJkzhBasicParam(), waterWhichEnum)))
				//添加元素标记处理器
				.addHandler(new AppendSubscriptHandler().setParams(Constant.FlagString))
				//添加展开公式处理器
				.addHandler(new ExpansionHandler().setParams(new ExpansionParam(time,beginFloor,endFloor)))
				//公式修正处理器
				.addHandler(new ReviseHandler().setParams(ReviseEnum.公式修正))
				//添加值填充处理器
				.addHandler(new FillValueHandler().setParams(jkzhGetValues));
		return latexFromulaEntity;
	}

	/**
	 * 固定公式展示处理器：
	 * 1、公式替换元素处理器
	 * @param fromula
	 * @param iLayout
	 * @return
	 */
	public FromulaEntity replaceLayoutChar(String fromula,ILayout iLayout){
		//用于word展示
		FromulaEntity latexFromulaEntity = new FromulaEntity(fromula);
		latexFromulaEntity
				//公式替换元素处理器
				.addHandler(new ReplaceLayoutHandler().setParams(iLayout));
		return latexFromulaEntity;
	}

	/**
	 * 固定公式展示处理器：
	 * 1、公式替换元素处理器
	 * 2、标记元素处理器
	 * 3、展开公式处理器
	 * @param fromula
	 * @param iLayout
	 * @return
	 */
	public FromulaEntity replaceExtendToCal(String fromula,
											ExpansionParam expansionParam,
											ILayout iLayout){
		//用于计算结果
		FromulaEntity calFromulaEntity = new FromulaEntity(fromula);
		calFromulaEntity
				//公式替换元素处理器
				.addHandler(new ReplaceLayoutHandler().setParams(iLayout))
				//添加元素标记处理器
				.addHandler(new AppendSubscriptHandler().setParams(Constant.FlagString))
				//添加展开公式处理器
				.addHandler(new ExpansionHandler().setParams(expansionParam));
		return calFromulaEntity;
	}

	/**
	 * 解方程处理器
	 * @param leftFromula 等号左边方程
	 * @param rightFromula 等号右边方程
	 * @return
	 */
	public FromulaEntity solveEquationsToCal(String leftFromula,String rightFromula){
		//用于计算结果
		FromulaEntity calFromulaEntity = new FromulaEntity(leftFromula +"=" + rightFromula);
		calFromulaEntity
				//添加值填充处理器
				.addHandler(new SolveEquationsHandler());
		return calFromulaEntity;
	}

	/**
	 * 需扩展计算结果
	 * @param jkzhGetValues
	 * @param fromula
	 * @return
	 */
	public FromulaEntity extendToCal(int curFloor,
									 JkzhGetValues jkzhGetValues,
									 String fromula){
		//用于word展示
		FromulaEntity latexFromulaEntity = new FromulaEntity(fromula);
		latexFromulaEntity
				//添加元素标记处理器
				.addHandler(new AppendSubscriptHandler().setParams(Constant.FlagString))
				//添加展开公式处理器
				.addHandler(new ExpansionHandler().setParams(new ExpansionParam(curFloor,curFloor,curFloor)))
				//添加值填充处理器
				.addHandler(new FillValueHandler().setParams(jkzhGetValues))
				//添加值填充处理器
				.addHandler(new CalHandler());
		return latexFromulaEntity;
	}

	/**
	 * 需扩展公式展示
	 * @param jkzhGetValues
	 * @param fromula
	 * @return
	 */
	public FromulaEntity extendToLatex(int curFloor,
									   JkzhGetValues jkzhGetValues,
									   String fromula){
		//用于word展示
		FromulaEntity latexFromulaEntity = new FromulaEntity(fromula);
		latexFromulaEntity
				//添加元素标记处理器
				.addHandler(new AppendSubscriptHandler().setParams(Constant.FlagString))
				//添加展开公式处理器
				.addHandler(new ExpansionHandler().setParams(new ExpansionParam(curFloor,curFloor,curFloor)))
				//添加值填充处理器
				.addHandler(new FillValueHandler().setParams(jkzhGetValues));
		return latexFromulaEntity;
	}

	/**
	 * 需扩展计算结果
	 * @param jkzhGetValues
	 * @param fromula
	 * @return
	 */
	public FromulaEntity extendToCalN(int time,
									  int beginFloor,
									  int endFloor,
									 JkzhGetValues jkzhGetValues,
									 String fromula){
		//用于word展示
		FromulaEntity latexFromulaEntity = new FromulaEntity(fromula);
		latexFromulaEntity
				//添加元素标记处理器
				.addHandler(new AppendSubscriptHandler().setParams(Constant.FlagString))
				//添加展开公式处理器
				.addHandler(new ExpansionHandler().setParams(new ExpansionParam(time,beginFloor,endFloor)))
				//添加值填充处理器
				.addHandler(new FillValueHandler().setParams(jkzhGetValues))
				//添加值填充处理器
				.addHandler(new CalHandler());
		return latexFromulaEntity;
	}

	/**
	 * 需扩展公式展示
	 * @param jkzhGetValues
	 * @param fromula
	 * @return
	 */
	public FromulaEntity extendToLatexN(int time,
										int beginFloor,
										int endFloor,
									   JkzhGetValues jkzhGetValues,
									   String fromula){
		//用于word展示
		FromulaEntity latexFromulaEntity = new FromulaEntity(fromula);
		latexFromulaEntity
				//添加元素标记处理器
				.addHandler(new AppendSubscriptHandler().setParams(Constant.FlagString))
				//添加展开公式处理器
				.addHandler(new ExpansionHandler().setParams(new ExpansionParam(time,beginFloor,endFloor)))
				//添加值填充处理器
				.addHandler(new FillValueHandler().setParams(jkzhGetValues));
		return latexFromulaEntity;
	}
}
