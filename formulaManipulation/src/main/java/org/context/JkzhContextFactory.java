package org.context;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.element.BaseElement;
import org.calParam.JkzhBasicParam;
import org.table.SoilPressureTable;
import org.table.SoilQualityTable;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class JkzhContextFactory {

    /**
     * 在获取基坑支护上下文的时候，通过jkzhCreateContextHandle
     * 计算 土压力系数表
     * @return
     */
    public static JkzhContext getJkzhContext(List<JkzhBasicParam> jkzhBasicParams, String[][] table){
        //①、生成土压力系数表
        JkzhContext jkzhContext = (JkzhContext) FactoryContext.getContext(JkzhContext.class);
        HashMap<String,String> temporaryValue = new HashMap<>(128);
        jkzhContext.setTemporaryValue(temporaryValue);

        HashMap<String, BaseElement> elementTemplate = new HashMap<>(128);
        jkzhContext.setElementTemplate(elementTemplate);

        //基础参数拼装
        jkzhContext.setJkzhBasicParams(jkzhBasicParams);
        SoilQualityTable soilQualityTable = new SoilQualityTable();
        soilQualityTable.setTable(table);
        jkzhContext.setSoilQualityTable(soilQualityTable);

        //土压力系数表计算
        createContextHandle(jkzhContext);
        return jkzhContext;
    }

    private static void createContextHandle(JkzhContext jkzhContext) {
        SoilPressureTable soilPressureTable = new SoilPressureTable(jkzhContext);
        jkzhContext.setSoilPressureTable(soilPressureTable);
        log.info("土地压力系数表:{}", JSON.toJSONString(soilPressureTable));
    }
}
