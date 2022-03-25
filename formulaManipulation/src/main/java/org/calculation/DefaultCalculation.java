package org.calculation;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.context.AbstractContext;
import org.context.FactoryContext;
import org.context.JkzhContext;
import org.element.BaseElement;
import org.table.JkzhBasicParam;
import org.table.SoilPressureTable;
import org.table.SoilQualityTable;

import java.util.HashMap;
@Slf4j
public class DefaultCalculation implements ICalculation {

    @Override
    public AbstractContext getContext(ICalculation iCalculation) {
        AbstractContext abstractContext = null;
        if(iCalculation instanceof JkzhCalculation){
            abstractContext = getJkzhContext();
        }
        return abstractContext;
    }

    /**
     * 在获取基坑支护上下文的时候，通过jkzhCreateContextHandle
     * 计算 土压力系数表
     * @return
     */
    private JkzhContext getJkzhContext(){
        //①、生成土压力系数表
        JkzhContext jkzhContext = (JkzhContext) FactoryContext.getContext(JkzhContext.class);
        HashMap<String,String> temporaryValue = new HashMap<>(128);
        jkzhContext.setTemporaryValue(temporaryValue);

        HashMap<String, BaseElement> elementTemplate = new HashMap<>(128);
        jkzhContext.setElementTemplate(elementTemplate);

        //基础参数拼装
        JkzhBasicParam jkzhBasicParam = new JkzhBasicParam();
        jkzhContext.setJkzhBasicParam(jkzhBasicParam);
        SoilQualityTable soilQualityTable = new SoilQualityTable();
        jkzhContext.setSoilQualityTable(soilQualityTable);
        //土压力系数表计算
        createContextHandle(jkzhContext);
        //总土层数
        calAllLands(soilQualityTable,jkzhBasicParam);
        //计算主动土层 水位所在第几层土
        calZDWaterLand(soilQualityTable,jkzhBasicParam);
        //计算被动土层 水位所在第几层土
        calBDWaterLand(soilQualityTable,jkzhBasicParam);
        //计算开挖面所在第几层土
        calDepthLand(soilQualityTable,jkzhBasicParam);
        return jkzhContext;
    }

    private void createContextHandle(AbstractContext AbstractContext) {
        JkzhContext jkzhContext = (JkzhContext)AbstractContext;
        SoilPressureTable soilPressureTable = new SoilPressureTable(jkzhContext);
        jkzhContext.setSoilPressureTable(soilPressureTable);
        log.info("土地压力系数表:{}", JSON.toJSONString(soilPressureTable));
    }

    /**
     * 总土层数
     * @param soilQualityTable 土层参数表
     * @param jkzhBasicParam 基础参数
     */
    private void calAllLands(SoilQualityTable soilQualityTable,JkzhBasicParam jkzhBasicParam){
        jkzhBasicParam.setAllLands(soilQualityTable.getTable().length -1);
    }

    /**
     * 计算主动土层 水位所在第几层土
     * @param soilQualityTable 土层参数表
     * @param jkzhBasicParam 基础参数
     */
    private void calZDWaterLand(SoilQualityTable soilQualityTable,JkzhBasicParam jkzhBasicParam){
        Double addm = 0.0;
        //计算开挖深度这层土的剩余厚度
        for (int floor = 1; floor <= jkzhBasicParam.getAllLands();floor++) {
            String hdValue = soilQualityTable.getTable()[floor][2];
            addm += Double.valueOf(hdValue);
            if(addm.compareTo(jkzhBasicParam.getZDWarterDepth())>=0){
                jkzhBasicParam.setZDWaterLand(floor);
                break;
            }
        }
    }

    /**
     * 计算被动土层 水位所在第几层土
     * @param soilQualityTable 土层参数表
     * @param jkzhBasicParam 基础参数
     */
    private void calBDWaterLand(SoilQualityTable soilQualityTable,JkzhBasicParam jkzhBasicParam){
        Double addm = 0.0;
        //计算开挖深度这层土的剩余厚度
        for (int floor = 1; floor <= jkzhBasicParam.getAllLands();floor++) {
            String hdValue = soilQualityTable.getTable()[floor][2];
            addm += Double.valueOf(hdValue);
            if(addm.compareTo(jkzhBasicParam.getBDWarterDepth())>=0){
                jkzhBasicParam.setBDWaterLand(floor);
                break;
            }
        }
    }


    /**
     * 计算开挖面所在第几层土
     * @param soilQualityTable 土层参数表
     * @param jkzhBasicParam 基础参数
     */
    private void calDepthLand(SoilQualityTable soilQualityTable,JkzhBasicParam jkzhBasicParam){
        Double addm = 0.0;
        //计算开挖深度这层土的剩余厚度
        for (int floor = 1; floor <= jkzhBasicParam.getAllLands();floor++) {
            String hdValue = soilQualityTable.getTable()[floor][2];
            addm += Double.valueOf(hdValue);
            if(addm.compareTo(jkzhBasicParam.getDepth())>=0){
                jkzhBasicParam.setAtDepthLand(floor);
                break;
            }
        }
    }
}
