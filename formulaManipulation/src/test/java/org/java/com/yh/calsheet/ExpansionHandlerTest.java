package org.java.com.yh.calsheet;

import lombok.extern.slf4j.Slf4j;
import org.entity.ExpansionParam;
import org.handler.ExpansionHandler;
import org.junit.Test;

@Slf4j
public class ExpansionHandlerTest {

    @Test
    public void expansionHandlerTest() {
        String abc = "(支撑轴力主动_{n}-支撑轴力被动_{n})-[支撑轴力_{n}  \\times 支撑轴臂_{n}-...]/(土压力零点_{n}-支撑的轴线_{n})";
        ExpansionHandler expansionHandler = new ExpansionHandler().setParams(new ExpansionParam(2,1,3));
        String tempFromula = expansionHandler.execute(abc);
        log.info("展开:{}",tempFromula);
    }
}
