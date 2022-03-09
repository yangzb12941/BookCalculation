package org.handle;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.context.IContext;
import org.context.JkzhContext;
import org.table.SoilPressureTable;

@Slf4j
public class JkzhCreateContextHandle implements ICreateContextHandle<JkzhFromulaHandle>{
    @Override
    public void createContextHandle(IContext iContext, JkzhFromulaHandle jkzhFromulaHandle) {
        JkzhContext jkzhContext = (JkzhContext)iContext;
        SoilPressureTable soilPressureTable = new SoilPressureTable(jkzhContext,jkzhFromulaHandle);
        jkzhContext.setSoilPressureTable(soilPressureTable);
        log.info("土地压力系数表:{}", JSON.toJSONString(soilPressureTable));
    }
}
