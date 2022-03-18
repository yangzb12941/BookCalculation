package org.calculation;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import org.context.AbstractContext;
import org.context.FactoryContext;
import org.context.JkzhContext;
import org.element.BaseElement;
import org.handle.FromulaHandle;
import org.handle.JkzhFromulaHandle;
import org.table.JkzhBasicParam;
import org.table.SoilPressureTable;
import org.table.SoilQualityTable;

import java.util.HashMap;
@Slf4j
public class DefaultCalculation extends AbstractCalculation{

    @Override
    public AbstractContext getContext(ICalculation iCalculation,
                                      FromulaHandle fromulaHandle) {
        AbstractContext abstractContext = null;
        if(iCalculation instanceof JkzhCalculation){
            abstractContext = getJkzhContext((JkzhFromulaHandle)fromulaHandle);
        }
        return abstractContext;
    }

    /**
     * 在获取基坑支护上下文的时候，通过jkzhCreateContextHandle
     * 计算 土压力系数表
     * @param jkzhFromulaHandle
     * @return
     */
    private JkzhContext getJkzhContext(JkzhFromulaHandle jkzhFromulaHandle){
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
        createContextHandle(jkzhContext,jkzhFromulaHandle);
        return jkzhContext;
    }

    private void createContextHandle(AbstractContext AbstractContext, JkzhFromulaHandle jkzhFromulaHandle) {
        JkzhContext jkzhContext = (JkzhContext)AbstractContext;
        SoilPressureTable soilPressureTable = new SoilPressureTable(jkzhContext,jkzhFromulaHandle);
        jkzhContext.setSoilPressureTable(soilPressureTable);
        log.info("土地压力系数表:{}", JSON.toJSONString(soilPressureTable));
    }
}
