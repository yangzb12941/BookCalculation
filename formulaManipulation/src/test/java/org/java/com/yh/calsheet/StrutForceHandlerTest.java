package org.java.com.yh.calsheet;

import lombok.extern.slf4j.Slf4j;
import org.entity.ExpansionParam;
import org.handleParams.StrutForceHandlerParam;
import org.handler.ExpansionHandler;
import org.handler.StrutForceHandler;
import org.junit.Test;

@Slf4j
public class StrutForceHandlerTest {

    @Test
    public void strutForceHandlerTest() {
        String abc = "(支撑轴力主动-支撑轴力被动)<④-[支撑轴力  \\times 支撑轴臂-...]>/(土压力零点-支撑的轴线)";
        StrutForceHandler strutForceHandler = new StrutForceHandler().setParams(new StrutForceHandlerParam(2));
        String tempFromula = strutForceHandler.execute(abc);
        log.info("展开:{}",tempFromula);
    }
}
