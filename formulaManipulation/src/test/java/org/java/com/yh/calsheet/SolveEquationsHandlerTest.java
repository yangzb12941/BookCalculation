package org.java.com.yh.calsheet;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.context.FactoryContext;
import org.context.JkzhContext;
import org.handler.SolveEquationsHandler;
import org.junit.Test;
import org.table.SoilPressureTable;
import org.table.SoilQualityTable;

@Slf4j
public class SolveEquationsHandlerTest {
    @Test
    public void execute(){
        SolveEquationsHandler SolveEquationsHandler = new SolveEquationsHandler();
        String execute = SolveEquationsHandler.execute("(20.0+18*1.6+18.3*1.4+17.4*1.0+18.7*2.3+17.4*x)*0.56-2.0*13.0*0.75=(18.7*2.3+17.4*x)*1.80+2.0*13.0*1.34");
        log.info("SoilPressureTable {}", JSONObject.toJSONString(execute));
    }
}
