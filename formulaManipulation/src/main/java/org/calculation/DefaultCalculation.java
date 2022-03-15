package org.calculation;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.context.FactoryContext;
import org.context.IContext;
import org.context.JkzhContext;
import org.handle.FromulaHandle;
import org.handle.JkzhFromulaHandle;
import org.table.JkzhBasicParam;
import org.table.SoilPressureTable;
import org.table.SoilQualityTable;

import java.util.HashMap;
@Slf4j
public class DefaultCalculation extends AbstractCalculation{

    @Override
    public IContext getContext(ICalculation iCalculation,
                               FromulaHandle fromulaHandle) {
        IContext iContext = null;
        if(iCalculation instanceof JkzhCalculation){
            iContext = getJkzhContext((JkzhFromulaHandle)fromulaHandle);
        }
        return iContext;
    }

    /**
     * 在获取基坑支护上下文的时候，通过jkzhCreateContextHandle
     * 计算 土压力系数表
     * @param jkzhFromulaHandle
     * @return
     */
    private JkzhContext getJkzhContext(JkzhFromulaHandle jkzhFromulaHandle){
        //①、生成土压力系数表
        JkzhContext jkzhContext = (JkzhContext)FactoryContext.getContext(JkzhContext.class);
        HashMap<String,String> formate = new HashMap<>(64);
        jkzhContext.setTemporaryValue(formate);
        //基础参数拼装
        JkzhBasicParam jkzhBasicParam = new JkzhBasicParam();
        SoilQualityTable soilQualityTable = new SoilQualityTable();
        jkzhBasicParam.setSoilQualityTable(soilQualityTable);
        jkzhContext.setJkzhBasicParam(jkzhBasicParam);
        //土压力系数表计算
        createContextHandle(jkzhContext,jkzhFromulaHandle);
        return jkzhContext;
    }

    private void createContextHandle(IContext iContext, JkzhFromulaHandle jkzhFromulaHandle) {
        JkzhContext jkzhContext = (JkzhContext)iContext;
        SoilPressureTable soilPressureTable = new SoilPressureTable(jkzhContext,jkzhFromulaHandle);
        jkzhContext.setSoilPressureTable(soilPressureTable);
        log.info("土地压力系数表:{}", JSON.toJSONString(soilPressureTable));
    }
}
