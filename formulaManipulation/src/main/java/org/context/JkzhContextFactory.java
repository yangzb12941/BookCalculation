package org.context;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.element.BaseElement;
import org.calParam.JkzhBasicParam;
import org.table.SoilPressureTable;
import org.table.SoilQualityTable;

import java.util.ArrayList;
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
        //初始化当前工况为0
        jkzhContext.setCalTimes(0);
        List<HashMap<String,String>> temporaryValues = new ArrayList<HashMap<String,String>>(jkzhBasicParams.size());
        HashMap<String,String> temporaryValue = new HashMap<String, String>(32);
        //下标为0，用于保存多工况以外的文档计算数据
        temporaryValues.add(jkzhContext.getCalTimes(),temporaryValue);
        jkzhContext.setTemporaryValues(temporaryValues);

        List<HashMap<String, BaseElement>> elementTemplates = new ArrayList<HashMap<String,BaseElement>>(jkzhBasicParams.size());
        HashMap<String, BaseElement> elementTemplate = new HashMap<String,BaseElement>(32);
        //下标为0，用于保存多工况以外的文档展示数据
        elementTemplates.add(jkzhContext.getCalTimes(),elementTemplate);
        jkzhContext.setElementTemplates(elementTemplates);
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
