package org.calculation;

import org.context.FactoryContext;
import org.context.IContext;
import org.context.JkzhContext;
import org.handle.FromulaHandle;
import org.handle.ICreateContextHandle;
import org.handle.JkzhCreateContextHandle;
import org.handle.JkzhFromulaHandle;
import org.table.JkzhBasicParam;
import org.table.SoilQualityTable;

import java.util.HashMap;
import java.util.Objects;

public class DefaultCalculation extends AbstractCalculation{

    @Override
    public IContext getContext(ICalculation iCalculation,
                               FromulaHandle fromulaHandle,
                               ICreateContextHandle iCreateContextHandle) {
        IContext iContext = null;
        if(iCalculation instanceof JkzhCalculation){
            iContext = getJkzhContext((JkzhCreateContextHandle) iCreateContextHandle,
                    (JkzhFromulaHandle)fromulaHandle);
        }
        return iContext;
    }

    /**
     * 在获取基坑支护上下文的时候，通过jkzhCreateContextHandle
     * 计算 土压力系数表
     * @param jkzhCreateContextHandle
     * @param jkzhFromulaHandle
     * @return
     */
    private JkzhContext getJkzhContext(JkzhCreateContextHandle jkzhCreateContextHandle,
                                       JkzhFromulaHandle jkzhFromulaHandle){
        //①、生成土压力系数表
        JkzhContext jkzhContext = (JkzhContext)FactoryContext.getContext(JkzhContext.class);
        HashMap<String,String> formate = new HashMap<>(36);
        jkzhContext.setFormate(formate);
        //基础参数拼装
        JkzhBasicParam jkzhBasicParam = new JkzhBasicParam();
        SoilQualityTable soilQualityTable = new SoilQualityTable();
        jkzhBasicParam.setSoilQualityTable(soilQualityTable);
        jkzhContext.setJkzhBasicParam(jkzhBasicParam);
        if(Objects.nonNull(jkzhCreateContextHandle)){
            //土压力系数表计算
            jkzhCreateContextHandle.createContextHandle(jkzhContext,jkzhFromulaHandle);
        }
        return jkzhContext;
    }
}
