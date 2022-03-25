package org.java.com.yh.calsheet;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.context.FactoryContext;
import org.context.JkzhContext;
import org.junit.Test;
import org.table.SoilPressureTable;
import org.table.SoilQualityTable;

@Slf4j
public class SoilPressureTableTest {
    @Test
    public void execute(){
        JkzhContext context = (JkzhContext)FactoryContext.getContext(JkzhContext.class);
        SoilQualityTable tableSQT = new SoilQualityTable();
        context.setSoilQualityTable(tableSQT);
        SoilPressureTable soilPressureTable = new SoilPressureTable(context);
        String[][] tableSPT = soilPressureTable.getTable();
        log.info("SoilPressureTable {}", JSONObject.toJSONString(tableSPT));
    }
}
